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

        classButton.setOnClickListener {
            startActivity(Intent(this, TeacherClassActivity::class.java))
            finish()
        }

        logoutButton.setOnClickListener {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
    }
}