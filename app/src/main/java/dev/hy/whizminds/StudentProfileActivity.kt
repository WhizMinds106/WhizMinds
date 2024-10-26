package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var etStudentName: EditText
    private lateinit var etStudentAge: EditText
    private lateinit var etStudentEmail: EditText
    private lateinit var etMathClassJoined: EditText
    private lateinit var etEnglishClassJoined: EditText
    private lateinit var btnBackStudent: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        etStudentName = findViewById(R.id.etStudentName)
        etStudentAge = findViewById(R.id.etStudentAge)
        etStudentEmail = findViewById(R.id.etStudentEmail)
        etMathClassJoined = findViewById(R.id.etMathClassJoined)
        etEnglishClassJoined = findViewById(R.id.etEnglishClassJoined)
        btnBackStudent = findViewById(R.id.btnBackStudent)

        fetchStudentDetails()

        btnBackStudent.setOnClickListener {
            startActivity(Intent(this, StudentDashboardActivity::class.java))
            finish()
        }
    }

    private fun fetchStudentDetails() {
        val currentStudentId = auth.currentUser?.uid
        val email = auth.currentUser?.email

        if (currentStudentId != null) {
            firestore.collection("users").document(currentStudentId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        etStudentName.setText(document.getString("name") ?: "")
                        etStudentAge.setText(document.getString("age") ?: "")
                        etStudentEmail.setText(email ?: "")

                        val mathClassCode = document.getString("mathClassCode")
                        val englishClassCode = document.getString("englishClassCode")

                        // Fetch the section name for Math class
                        if (mathClassCode != null) {
                            fetchSectionName(mathClassCode) { sectionName ->
                                etMathClassJoined.setText(sectionName ?: "Not Joined Math Class")
                            }
                        } else {
                            etMathClassJoined.setText("Not Joined Math Class")
                        }

                        // Fetch the section name for English class
                        if (englishClassCode != null) {
                            fetchSectionName(englishClassCode) { sectionName ->
                                etEnglishClassJoined.setText(sectionName ?: "Not Joined English Class")
                            }
                        } else {
                            etEnglishClassJoined.setText("Not Joined English Class")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle Errors here if needed
                }
        }
    }

    private fun fetchSectionName(sectionCode: String, callback: (String?) -> Unit) {
        firestore.collection("sections")
            .whereEqualTo("sectionCode", sectionCode)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val sectionName = querySnapshot.documents.firstOrNull()?.getString("sectionName")
                callback(sectionName)
            }
            .addOnFailureListener { e ->
                // Log error or handle error if needed
                callback(null)
            }
    }
}