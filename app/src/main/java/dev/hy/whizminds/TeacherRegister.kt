package dev.hy.whizminds

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherRegister : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        cancelButton.setOnClickListener {
            startActivity(Intent(this, TeacherMainActivity::class.java))
            finish()
        }

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Get the selected sex from the radio group
            val selectedSexId = sexRadioGroup.checkedRadioButtonId
            val sex = if (selectedSexId != -1) {
                findViewById<RadioButton>(selectedSexId).text.toString()
            } else {
                ""
            }

            if (name.isNotEmpty() && age.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerTeacher(name, age, sex, email, password)
                startActivity(Intent(this, TeacherDashboard::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerTeacher(name: String, age: String, sex: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Save additional user data in Firestore
                    Log.d(TAG, "createUserWithEmail:success")
                    saveTeacherInfo(auth.currentUser?.uid, name, age, sex, email)

                    startActivity(Intent(this, TeacherDashboard::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "createUserWithEmail:failure", task.exception)
                }
            }
    }

    private fun saveTeacherInfo(userId: String?, name: String, age: String, sex: String, email: String) {
        val teacherData = hashMapOf(
            "name" to name,
            "age" to age,
            "sex" to sex,
            "email" to email,
            "userType" to "Teacher"
        )

        if (userId != null) {
            firestore.collection("users").document(userId)
                .set(teacherData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    //Navigate to teacher Dashboard
                    startActivity(Intent(this, TeacherDashboard::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}