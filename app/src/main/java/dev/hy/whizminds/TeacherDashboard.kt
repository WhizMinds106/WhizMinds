package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class TeacherDashboard : AppCompatActivity() {

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
                    settingsButton.setBackgroundResource(R.drawable.settings_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    settingsButton.setBackgroundResource(R.drawable.settings)
                }
            }
            false
        }
        settingsButton.setOnClickListener {
            startActivity(Intent(this, TeacherSettingsActivity::class.java))
        }

        classButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    classButton.setBackgroundResource(R.drawable.class_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    classButton.setBackgroundResource(R.drawable.class_a)
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
                    logoutButton.setBackgroundResource(R.drawable.logout_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    logoutButton.setBackgroundResource(R.drawable.logout)
                }
            }
            false
        }

        logoutButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        profileButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    profileButton.setBackgroundResource(R.drawable.profile_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    profileButton.setBackgroundResource(R.drawable.profile)
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
                    studentsButton.setBackgroundResource(R.drawable.students_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    studentsButton.setBackgroundResource(R.drawable.students)
                }
            }
            false
        }

        aboutButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    aboutButton.setBackgroundResource(R.drawable.about_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    aboutButton.setBackgroundResource(R.drawable.about)
                }
            }
            false
        }
        aboutButton.setOnClickListener {
            startActivity(Intent(this, TeacherAboutActivity::class.java))
            finish()
        }

        activityButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    activityButton.setBackgroundResource(R.drawable.activities_p)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    activityButton.setBackgroundResource(R.drawable.activities)
                }
            }
            false
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you wanted to exit ${getString(R.string.app_name)}?")
            .setPositiveButton("Yes") { dialog, which ->
                // User confirmed
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
            .setNegativeButton("No") { dialog, which ->
                // User cancelled do nothing
            }
            .setTitle("Alert!")
            .setIcon(R.drawable.icon)
            .show()
    }

}