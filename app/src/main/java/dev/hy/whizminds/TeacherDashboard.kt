package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
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
        val settingsButton = findViewById<Button>(R.id.btnSettings)

        settingsButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    settingsButton.setBackgroundResource(R.drawable.btn_settings_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    settingsButton.setBackgroundResource(R.drawable.btn_settings)
                }
            }
            false
        }

        classButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    classButton.setBackgroundResource(R.drawable.btn_class_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    classButton.setBackgroundResource(R.drawable.btn_class)
                }
            }
            false
        }

        classButton.setOnClickListener {
            startActivity(Intent(this, TeacherClassActivity::class.java))
            finish()
        }

        logoutButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    logoutButton.setBackgroundResource(R.drawable.btn_logout_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    logoutButton.setBackgroundResource(R.drawable.btn_logout)
                }
            }
            false
        }

        logoutButton.setOnClickListener {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }

        profileButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    profileButton.setBackgroundResource(R.drawable.btn_pro_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    profileButton.setBackgroundResource(R.drawable.btn_pro)
                }
            }
            false
        }

        profileButton.setOnClickListener {
            startActivity(Intent(this, TeacherProfileActivity::class.java))
            finish()
        }

        studentsButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    studentsButton.setBackgroundResource(R.drawable.btn_students_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    studentsButton.setBackgroundResource(R.drawable.btn_students)
                }
            }
            false
        }

        aboutButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    aboutButton.setBackgroundResource(R.drawable.btn_about_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    aboutButton.setBackgroundResource(R.drawable.btn_about)
                }
            }
            false
        }

        activityButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    activityButton.setBackgroundResource(R.drawable.btn_act_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    activityButton.setBackgroundResource(R.drawable.btn_act)
                }
            }
            false
        }
    }
}