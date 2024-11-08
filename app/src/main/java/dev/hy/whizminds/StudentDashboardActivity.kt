package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button

class StudentDashboardActivity : AppCompatActivity() {
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

        setContentView(R.layout.activity_student_dashboard)

        val classButton = findViewById<Button>(R.id.btnClass)
        val profileButton = findViewById<Button>(R.id.btnProfile)
        val logoutButton = findViewById<Button>(R.id.btnLogout)
        val activitiesButton = findViewById<Button>(R.id.btnActivity)
        val aboutButton = findViewById<Button>(R.id.btnAbout)
        val settingButton = findViewById<Button>(R.id.btnSettings)

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

        settingButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    settingButton.setBackgroundResource(R.drawable.btn_settings_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    settingButton.setBackgroundResource(R.drawable.btn_settings)
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
            startActivity(Intent(this, StudentClassActivity::class.java))
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
            startActivity(Intent(this, StudentProfileActivity::class.java))
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

        activitiesButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    activitiesButton.setBackgroundResource(R.drawable.btn_act_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    activitiesButton.setBackgroundResource(R.drawable.btn_act)
                }
            }
            false
        }

    }
}