package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentClassActivity : AppCompatActivity() {

    private lateinit var etMathCode: EditText
    private lateinit var etEnglishCode: EditText
    private lateinit var btnJoinClass: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_class)

        val backButton = findViewById<Button>(R.id.btnBackStudent)

        backButton.setOnClickListener {
            startActivity(Intent(this, StudentDashboardActivity::class.java))
            finish()
        }

        etMathCode = findViewById(R.id.etMathCode)
        etEnglishCode = findViewById(R.id.etEnglishCode)
        btnJoinClass = findViewById(R.id.btnJoinClass)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        btnJoinClass.setOnClickListener {
            val mathCode = etMathCode.text.toString().trim()
            val englishCode = etEnglishCode.text.toString().trim()
            val currentStudentId = auth.currentUser?.uid

            if (currentStudentId != null) {
                if (mathCode.isNotEmpty() && englishCode.isNotEmpty()) {
                    joinClass(currentStudentId, mathCode, englishCode)
                } else {
                    Toast.makeText(this, "Please enter class code completely", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun joinClass(studentId: String, mathCode: String, englishCode: String) {
        val studentRef = firestore.collection("users").document(studentId)

        studentRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val updates = mutableMapOf<String, Any>()

                if (mathCode.isNotEmpty()) {
                    updates["mathClassCode"] = mathCode
                }
                if (englishCode.isNotEmpty()) {
                    updates["englishClassCode"] = englishCode
                }

                studentRef.update(updates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Successfully joined the class(es)", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to join class", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error fetching student data", Toast.LENGTH_SHORT).show()
        }
    }
}