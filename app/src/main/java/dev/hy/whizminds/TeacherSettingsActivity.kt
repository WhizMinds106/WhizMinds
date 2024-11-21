package dev.hy.whizminds

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Switch

class TeacherSettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

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

        setContentView(R.layout.activity_teacher_settings)

        val musicSwitch = findViewById<Switch>(R.id.switchBackgroundMusic)

        val btnBack = findViewById<Button>(R.id.btnBackTeacher)

        btnBack.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnBack.setBackgroundResource(R.drawable.btn_back_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    btnBack.setBackgroundResource(R.drawable.btn_back)
                }
            }
            false
        }
        btnBack.setOnClickListener {
            startActivity(Intent(this, TeacherDashboard::class.java))
            finish()
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isMusicEnabled = sharedPreferences.getBoolean("MusicEnabled", true)

        // Set the initial state of the switch
        musicSwitch.isChecked = isMusicEnabled
        if (isMusicEnabled) {
            startMusicService()
        }

        // Set up the toggle switch listener
        musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("MusicEnabled", isChecked)
            editor.apply()

            if (isChecked) {
                startMusicService()
            } else {
                stopMusicService()
            }
        }

    }

    private fun startMusicService() {
        val intent = Intent(this, BackgroundMusicService::class.java)
        startService(intent)
    }

    private fun stopMusicService() {
        val intent = Intent(this, BackgroundMusicService::class.java)
        stopService(intent)
    }

}