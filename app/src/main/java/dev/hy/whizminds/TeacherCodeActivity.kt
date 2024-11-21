package dev.hy.whizminds

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
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

        // Make the app fullscreen
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        setContentView(R.layout.activity_teacher_code)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val etSectionName = findViewById<EditText>(R.id.etSectionName)
        val btnGenerateCode = findViewById<Button>(R.id.btnGenerateCode)
        val tvGenerateCode = findViewById<TextView>(R.id.tvGeneratedCode)
        val gradeSpinner = findViewById<Spinner>(R.id.spinnerGrade)
        val subjectRadioGroup = findViewById<RadioGroup>(R.id.rgSubject)
        val btnBack = findViewById<Button>(R.id.btnBackTeacher)
        val btnCopyCode = findViewById<Button>(R.id.btnCopyCode)

        val grades = arrayOf("Select Grade", "Grade 1", "Grade 2", "Grade 3")
        val gradeAdapter = ArrayAdapter(this, R.layout.spinner_item, grades)
        gradeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        gradeSpinner.adapter = gradeAdapter

        btnGenerateCode.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnGenerateCode.setBackgroundResource(R.drawable.gen_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    btnGenerateCode.setBackgroundResource(R.drawable.gen)
                }
            }
            false
        }
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

        btnCopyCode.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnCopyCode.setBackgroundResource(R.drawable.copy_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    btnCopyCode.setBackgroundResource(R.drawable.copy)
                }
            }
            false
        }
        btnCopyCode.setOnClickListener {
            val generatedCode = tvGenerateCode.text.toString()
            if (generatedCode.isNotBlank()) {
                copyToClipboard(generatedCode)
                Toast.makeText(this, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No code to copy!", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnBack.setBackgroundResource(R.drawable.btn_back_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    btnBack.setBackgroundResource(R.drawable.btn_back)
                }
            }
            false
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

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Section Code", text)
        clipboard.setPrimaryClip(clip)
    }
}
