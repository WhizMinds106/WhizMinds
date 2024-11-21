package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var etTeacherName: EditText
    private lateinit var etTeacherAge: EditText
    private lateinit var etTeacherEmail: EditText
    private lateinit var tvSectionCount: TextView

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

        setContentView(R.layout.activity_teacher_profile)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val backButton = findViewById<Button>(R.id.btnBackTeacher)

        etTeacherName = findViewById(R.id.etTeacherName)
        etTeacherAge = findViewById(R.id.etTeacherAge)
        etTeacherEmail = findViewById(R.id.etTeacherEmail)
        tvSectionCount = findViewById(R.id.tvSectionCount)

        // Fetch teacher's details
        fetchTeacherDetails()

        // Fetch and display the number of sections
        fetchSectionCount()

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
            startActivity(Intent(this, TeacherDashboard::class.java))
            finish()
        }
    }

    private fun fetchTeacherDetails() {
        val currentTeacherId = auth.currentUser?.uid
        val email = auth.currentUser?.email

        if (currentTeacherId != null) {
            firestore.collection("users").document(currentTeacherId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        etTeacherName.setText(document.getString("name"))
                        etTeacherAge.setText(document.getString("age"))
                        etTeacherEmail.setText(email)
                    }
                }
                .addOnFailureListener {
                    // Handle errors (e.g., failed to retrieve user details)
                }
        }
    }

    private fun fetchSectionCount() {
        val currentTeacherId = auth.currentUser?.uid

        if (currentTeacherId != null) {
            firestore.collection("sections")
                .whereEqualTo("teacherId", currentTeacherId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val sectionCount = querySnapshot.size()
                    tvSectionCount.text = "Total Sections: $sectionCount"
                }
                .addOnFailureListener {
                    tvSectionCount.text = "Failed to load sections"
                }
        }
    }
}
