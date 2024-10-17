package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class TeacherUpdateSectionActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var etSectionName: EditText
    private lateinit var btnUpdateSection: Button
    private var sectionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_update_section)

        firestore = FirebaseFirestore.getInstance()

        etSectionName = findViewById(R.id.etSectionName)
        btnUpdateSection = findViewById(R.id.btnUpdateSection)

        val backButton = findViewById<Button>(R.id.btnBackTeacher)

        // Get section Id passed from the previous activity
        sectionId = intent.getStringExtra("sectionId")

        backButton.setOnClickListener {
            startActivity(Intent(this, TeacherClassActivity::class.java))
            finish()
        }

        btnUpdateSection.setOnClickListener {
            val newSectionName = etSectionName.text.toString().trim()
            if (newSectionName.isNotEmpty() && sectionId != null) {
                updateSection(sectionId!!, newSectionName)

            } else {
                Toast.makeText(this, "Please enter a section name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSection(sectionId: String, newSectionName: String) {
        firestore.collection("sections")
            .document(sectionId)
            .update("sectionName", newSectionName)
            .addOnSuccessListener {
                Toast.makeText(this, "Section updated successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, TeacherSectionsActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update section", Toast.LENGTH_SHORT).show()
            }
    }

}