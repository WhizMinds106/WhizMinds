package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StudentDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

        val classButton = findViewById<Button>(R.id.btnClass)
        val profileButton = findViewById<Button>(R.id.btnProfile)
        val logoutButton = findViewById<Button>(R.id.btnLogout)
        val activitiesButton = findViewById<Button>(R.id.btnActivity)

        classButton.setOnClickListener {
            startActivity(Intent(this, StudentClassActivity::class.java))
            finish()
        }

        profileButton.setOnClickListener {
            startActivity(Intent(this, StudentProfileActivity::class.java))
            finish()
        }

        logoutButton.setOnClickListener {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }

        activitiesButton.setOnClickListener {
            startActivity(Intent(this, StudentActivitiesActivity::class.java))
            finish()
        }
    }
}