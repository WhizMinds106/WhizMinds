package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pl.droidsonroids.gif.GifImageButton

class StudentMainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.etLoginEmail)
        val passwordEditText = findViewById<EditText>(R.id.etLoginPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerButton = findViewById<Button>(R.id.btnRegister)
        val forgotPassword = findViewById<Button>(R.id.btnForgotPassword)
        val buttonTeacher = findViewById<GifImageButton>(R.id.btn_teacher)
        val exitButton = findViewById<Button>(R.id.btnExitStudent)

        exitButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, StudentRegisterActivity::class.java))
            finish()
        }

        forgotPassword.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isNotEmpty()) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Enter your email first", Toast.LENGTH_SHORT).show()
            }
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Action for teacher button
        buttonTeacher.setOnClickListener {
            buttonTeacher.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .withEndAction {
                    val intent = Intent(this, TeacherMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }.start()
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    checkIfStudent(auth.currentUser?.uid)
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkIfStudent(userId: String?) {
        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()

            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userType = document.getString("userType")
                        if (userType == "Student") {
                            // Navigate to student dashboard
                            startActivity(Intent(this, StudentDashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you wanted to exit ${getString(R.string.app_name)}?")
            .setPositiveButton("Yes") { dialog, which ->
                // User confirmed
                finishAndRemoveTask()
            }
            .setNegativeButton("No") { dialog, which ->
                // User cancelled do nothing
            }
            .setTitle("Alert!")
            .setIcon(R.drawable.icon)
            .show()
    }
}