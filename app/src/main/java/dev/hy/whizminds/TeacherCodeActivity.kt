package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.util.Date

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
        val gradeSpinner = findViewById<Spinner>(R.id.spinnerGrade)
        val subjectRadioGroup = findViewById<RadioGroup>(R.id.rgSubject)
        val btnBack = findViewById<Button>(R.id.btnBackTeacher)

        val grades = arrayOf("Select Grade", "Grade 1", "Grade 2", "Grade 3")
        val gradeAdapter = ArrayAdapter(this, R.layout.spinner_item, grades)
        gradeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        gradeSpinner.adapter = gradeAdapter

        btnGenerateCode.setOnClickListener {
            val sectionName = etSectionName.text.toString().trim()

            val selectedGrade = gradeSpinner.selectedItem.toString()

            val selectedSubjectId = subjectRadioGroup.checkedRadioButtonId
            val selectedSubject = if (selectedSubjectId != -1) {
                findViewById<RadioButton>(selectedSubjectId).text.toString()
            } else {
                ""
            }

            Log.d("TeacherCodeActivity", "Section Name: $sectionName, Selected Grade: $selectedGrade, Selected Subject: $selectedSubject")

            if (selectedGrade == "Select Grade"){
                Toast.makeText(this, "Please select a grade", Toast.LENGTH_SHORT).show()
            }else if (sectionName.isNotEmpty() && selectedGrade.isNotEmpty() && selectedSubject.isNotEmpty()) {
                // Generate and save the section
                generateAndSaveSection(sectionName, selectedGrade, selectedSubject, tvGenerateCode)
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                Log.w("TeacherCodeActivity", "Some fields are missing")
            }
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, TeacherClassActivity::class.java))
            finish()
        }
    }

    private fun generateAndSaveSection(sectionName: String, grade: String, subject: String, tvGeneratedCode: TextView) {
        val sectionCode = generateSectionCode()

        val currentTeacherId = getCurrentTeacherId()
        if (currentTeacherId == null) {
            Toast.makeText(this, "Error: Teacher not logged in", Toast.LENGTH_SHORT).show()
            Log.e("TeacherCodeActivity", "No teacher logged in")
            return
        }

        // Get current date and time
        val currentTime = Timestamp(Date())  // Using java.util.Date to get the current time

        // Prepare data to save in Firestore, including the generated time
        val sectionData = hashMapOf(
            "sectionCode" to sectionCode,
            "sectionName" to sectionName,
            "grade" to grade,
            "subject" to subject,
            "teacherId" to currentTeacherId,
            "createdAt" to currentTime   // Add the timestamp here
        )

        firestore.collection("sections")
            .add(sectionData)
            .addOnSuccessListener {
                // Display the generated section code to the teacher
                tvGeneratedCode.text = "Generated section code: $sectionCode"
                tvGeneratedCode.visibility = TextView.VISIBLE
                Log.d("TeacherCodeActivity", "Section added successfully with code: $sectionCode at $currentTime")
                Toast.makeText(this, "Section generated successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("TeacherCodeActivity", "Error adding section", e)
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
