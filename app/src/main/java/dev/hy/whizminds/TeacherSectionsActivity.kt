package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherSectionsActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var sectionListView: ListView
    private lateinit var sectionAdapter: ArrayAdapter<String>
    private var sectionList = mutableListOf<String>()
    private var sectionIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the app fullscreen
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        setContentView(R.layout.activity_teacher_sections)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        sectionListView = findViewById(R.id.sectionListView)

        val backButton = findViewById<Button>(R.id.btnBackTeacher)

        backButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    backButton.setBackgroundResource(R.drawable.btn_back_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    backButton.setBackgroundResource(R.drawable.btn_back)
                }
            }
            false
        }
        backButton.setOnClickListener {
            startActivity(Intent(this, TeacherClassActivity::class.java))
            finish()
        }

        // Initialize array adapter
        sectionAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sectionList)
        sectionListView.adapter = sectionAdapter

        // Fetch sections created by the logged-in teacher
        fetchSections()

        // Handle item click for update and delete options
        sectionListView.setOnItemClickListener { _, _, position, _ ->
            val sectionId = sectionIds[position]
            val sectionName = sectionList[position]
            showUpdateDeleteDialog(sectionId, sectionName)
        }
    }

    private fun fetchSections() {
        val currentTeacherId = auth.currentUser?.uid

        if (currentTeacherId == null) {
            Toast.makeText(this, "Error: Teacher not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch sections from firestore
        firestore.collection("sections")
            .whereEqualTo("teacherId", currentTeacherId)
            .get()
            .addOnSuccessListener { result ->
                sectionList.clear()
                sectionIds.clear()
                for (document in result) {
                    val sectionName = document.getString("sectionName") ?: "Unnamed Section"
                    sectionList.add(sectionName)
                    sectionIds.add(document.id)
                }
                sectionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.w("TeacherSectionsActivity", "Error fetching sections", e)
                Toast.makeText(this, "Failed to load sections", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showUpdateDeleteDialog(sectionId: String, sectionName: String) {
        val options = arrayOf("Update", "Delete")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose option for $sectionName")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    // Update section
                    val intent = Intent(this, TeacherUpdateSectionActivity::class.java)
                    intent.putExtra("sectionId", sectionId)
                    startActivity(intent)
                }
                1 -> {
                    // Delete section
                    deleteSection(sectionId)
                }
            }
        }
        builder.show()
    }

    private fun deleteSection(sectionId: String) {
        firestore.collection("sections")
            .document(sectionId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Section deleted successfully", Toast.LENGTH_SHORT). show()
                fetchSections()
            }
            .addOnFailureListener { e ->
                Log.w("TeacherSectionsActivity", "Error deleting section", e)
                Toast.makeText(this, "Failed to delete section", Toast.LENGTH_SHORT).show()
            }
    }
}