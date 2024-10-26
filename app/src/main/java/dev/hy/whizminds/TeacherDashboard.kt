package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TeacherDashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_dashboard)

        val classButton = findViewById<Button>(R.id.btnClass)
        val logoutButton = findViewById<Button>(R.id.btnLogout)
        val profileButton = findViewById<Button>(R.id.btnProfile)
        val studentsButton = findViewById<Button>(R.id.btnStudents)
        val aboutButton = findViewById<Button>(R.id.btnAbout)
        val activityButton = findViewById<Button>(R.id.btnActivity)

        classButton.setOnClickListener {
            startActivity(Intent(this, TeacherClassActivity::class.java))
            finish()
        }

        logoutButton.setOnClickListener {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }

        profileButton.setOnClickListener {
            startActivity(Intent(this, TeacherProfileActivity::class.java))
            finish()
        }

        studentsButton.setOnClickListener {
            startActivity(Intent(this, TeacherStudentsActivity::class.java))
            finish()
        }

        aboutButton.setOnClickListener {
            startActivity(Intent(this, TeacherAboutActivity::class.java))
            finish()
        }

        activityButton.setOnClickListener {
            startActivity(Intent(this, TeacherActivitiesActivity::class.java))
            finish()
        }
    }
}