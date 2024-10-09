package dev.hy.whizminds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherCodeActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_code)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val etSectionName = findViewById<EditText>(R.id.etSectionName)
        val btnGenerateCode = findViewById<Button>(R.id.btnGenerateCode)
        val tvGenerateCode = findViewById<TextView>(R.id.tvGeneratedCode)

        btnGenerateCode.setOnClickListener {
            val sectionName = etSectionName.text.toString().trim()

            if (sectionName.isNotEmpty()) {
                // Generate and save the section
                generateAndSaveSection(sectionName, tvGenerateCode)
            } else {
                Toast.makeText(this, "Please enter a section name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateAndSaveSection(sectionName: String, tvGeneratedCode: TextView) {
        val sectionCode = generateSectionCode()

        val currentTeacherId = getCurrentTeacherId()
        if (currentTeacherId == null) {
            Toast.makeText(this, "Error: Teacher not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare data to save in Firestore
        val sectionData = hashMapOf(
            "sectionCode" to sectionCode,
            "sectionName" to sectionName,
            "teacherId" to currentTeacherId
        )

        firestore.collection("sections")
            .add(sectionData)
            .addOnSuccessListener {
                // Display the generated section code to the teacher
                tvGeneratedCode.text = "Generated section code: $sectionCode"
                tvGeneratedCode.visibility = TextView.VISIBLE
                Log.d("Firestore", "Section added successfully with code: $sectionCode")
                Toast.makeText(this, "Section generated successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding section", e)
                Toast.makeText(this, "Failed to generate section code", Toast.LENGTH_SHORT).show()
            }
    }

    // Method to generate a random 6-character section code
    private fun generateSectionCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        return (1..6).map { chars.random() }.joinToString("")
    }

    // Get the currently logged-in teacher's ID
    private fun getCurrentTeacherId(): String? {
        return auth.currentUser?.uid
    }
}
