package dev.hy.whizminds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentRegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val TAG = "StudentRegisterActivity"

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

        val grades = arrayOf("Select Grade", "Grade 1", "Grade 2", "Grade 3")
        val gradeAdapter = ArrayAdapter(this, R.layout.spinner_item, grades)
        gradeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        gradeSpinner.adapter = gradeAdapter

        cancelButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    cancelButton.setBackgroundResource(R.drawable.btn_back_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    cancelButton.setBackgroundResource(R.drawable.btn_back)
                }
            }
            false
        }

        cancelButton.setOnClickListener {
            startActivity(Intent(this, StudentMainActivity::class.java))
            finish()
        }

        registerButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    registerButton.setBackgroundResource(R.drawable.btn_register_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    registerButton.setBackgroundResource(R.drawable.btn_register)
                }
            }
            false
        }

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val selectedSexId = sexRadioGroup.checkedRadioButtonId
            val sex = if (selectedSexId != -1) {
                findViewById<RadioButton>(selectedSexId).text.toString()
            } else {
                ""
            }

            val selectedGrade = gradeSpinner.selectedItem.toString()

            if (selectedGrade == "Select Grade") {
                Toast.makeText(this, "Please select a grade", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Grade not selected")
            } else if (name.isNotEmpty() && age.isNotEmpty() && sex.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                Log.d(TAG, "All fields are filled, proceeding with registration")
                registerStudent(name, age, sex, selectedGrade, email, password)
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Missing fields: name=$name, age=$age, sex=$sex, email=$email, password=$password")
            }
        }
    }

    private fun registerStudent(name: String, age: String, sex: String, grade: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account created successfully")
                    saveStudentInfo(auth.currentUser?.uid, name, age, sex, grade, email)
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Registration failed: ${task.exception?.message}")
                }
            }
    }

    private fun saveStudentInfo(userId: String?, name: String, age: String, sex: String, grade: String, email: String) {
        if (userId == null) {
            Log.e(TAG, "User ID is null; failed to save student info")
            Toast.makeText(this, "User ID is null; could not save data.", Toast.LENGTH_SHORT).show()
            return
        }

        val studentData = hashMapOf(
            "name" to name,
            "age" to age,
            "sex" to sex,
            "grade" to grade,
            "email" to email,
            "userType" to "Student"
        )

        firestore.collection("users").document(userId)
            .set(studentData)
            .addOnSuccessListener {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Student info saved successfully in Firestore")
                startActivity(Intent(this, StudentMainActivity::class.java))
                finish()
                Toast.makeText(this, "Account Created! Please login now.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save the data: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failed to save student data: ${e.message}")
            }
    }
}
