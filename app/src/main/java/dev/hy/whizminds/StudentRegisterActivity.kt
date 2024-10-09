package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentRegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val nameEditText = findViewById<EditText>(R.id.etStudentName)
        val ageEditText = findViewById<EditText>(R.id.etStudentAge)
        val sexRadioGroup = findViewById<RadioGroup>(R.id.rgSex)
        val emailEditText = findViewById<EditText>(R.id.etStudentEmail)
        val passwordEditText = findViewById<EditText>(R.id.etStudentPassword)
        val registerButton = findViewById<Button>(R.id.btnRegisterStudent)
        val gradeSpinner = findViewById<Spinner>(R.id.spinnerGrade)
        val cancelButton = findViewById<Button>(R.id.btnCancelStudent)

        // Add "Select Grade" as the first option in the grades array
        val grades = arrayOf("Select Grade", "Grade 1", "Grade 2", "Grade 3")
        val gradeAdapter = ArrayAdapter(this, R.layout.spinner_item, grades)
        gradeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        gradeSpinner.adapter = gradeAdapter

        cancelButton.setOnClickListener {
            startActivity(Intent(this, StudentMainActivity::class.java))
            finish()
        }

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Get the selected sex from the RadioGroup
            val selectedSexId = sexRadioGroup.checkedRadioButtonId
            val sex = if (selectedSexId != -1) {
                findViewById<RadioButton>(selectedSexId).text.toString()
            } else {
                ""
            }

            // Get the selected grade from the Spinner
            val selectedGrade = gradeSpinner.selectedItem.toString()

            // Check if "Select Grade" is still selected
            if (selectedGrade == "Select Grade") {
                Toast.makeText(this, "Please select a grade", Toast.LENGTH_SHORT).show()
            } else if (name.isNotEmpty() && age.isNotEmpty() && sex.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerStudent(name, age, sex, selectedGrade, email, password)
                startActivity(Intent(this, StudentDashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerStudent(name: String, age: String, sex: String, grade: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Save student info in Firestore
                    saveStudentInfo(auth.currentUser?.uid, name, age, sex, grade, email)
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveStudentInfo(userId: String?, name: String, age: String, sex: String, grade: String, email: String) {
        val studentData = hashMapOf(
            "name" to name,
            "age" to age,
            "sex" to sex,
            "grade" to grade,
            "email" to email,
            "userType" to "Student"
        )

        if (userId != null) {
            firestore.collection("users").document(userId)
                .set(studentData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    // Navigate to the student's dashboard
                    startActivity(Intent(this, StudentDashboardActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save the data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
