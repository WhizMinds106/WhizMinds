package dev.hy.whizminds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherRegister : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val TAG = "TeacherRegisterActivity"

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

        setContentView(R.layout.activity_teacher_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val nameEditText = findViewById<EditText>(R.id.etTeacherName)
        val ageEditText = findViewById<EditText>(R.id.etteacherAge)
        val sexRadioGroup = findViewById<RadioGroup>(R.id.rgSex)
        val emailEditText = findViewById<EditText>(R.id.etTeacherEmail)
        val passwordEditText = findViewById<EditText>(R.id.etTeacherPassword)
        val registerButton = findViewById<Button>(R.id.btnRegisterTeacher)
        val cancelButton = findViewById<Button>(R.id.btnCancelTeacher)

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
            startActivity(Intent(this, TeacherMainActivity::class.java))
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

            if (name.isNotEmpty() && age.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                Log.d(TAG, "All fields are filled, proceeding with registration")
                registerTeacher(name, age, sex, email, password)
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Missing fields: name=$name, age=$age, sex=$sex, email=$email, password=$password")
            }
        }
    }

    private fun registerTeacher(name: String, age: String, sex: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account created successfully")
                    saveTeacherInfo(auth.currentUser?.uid, name, age, sex, email)
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Registration failed: ${task.exception?.message}")
                }
            }
    }

    private fun saveTeacherInfo(userId: String?, name: String, age: String, sex: String, email: String) {
        if (userId == null) {
            Log.e(TAG, "User ID is null; failed to save teacher info")
            Toast.makeText(this, "User ID is null; could not save data.", Toast.LENGTH_SHORT).show()
            return
        }

        val teacherData = hashMapOf(
            "name" to name,
            "age" to age,
            "sex" to sex,
            "email" to email,
            "userType" to "Teacher"
        )

        firestore.collection("users").document(userId)
            .set(teacherData)
            .addOnSuccessListener {
                Toast.makeText(this, "Registration Successful. You can log in now", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Teacher info saved successfully in Firestore")
                startActivity(Intent(this, TeacherMainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failed to save teacher data: ${e.message}")
            }
    }
}
