package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
