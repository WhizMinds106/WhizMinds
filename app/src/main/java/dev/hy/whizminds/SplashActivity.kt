package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.content.SharedPreferences
import android.widget.ImageButton

class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the app fullscreen
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Set the content view
        setContentView(R.layout.activity_splash)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val isMusicEnabled = sharedPreferences.getBoolean("MusicEnabled", true)

        // Start music service if enabled
        if (isMusicEnabled) {
            val intent = Intent(this, BackgroundMusicService::class.java)
            startService(intent)
        }

        // Button reference
        val myImageButton = findViewById<ImageButton>(R.id.btn_letsgo)

        // Logic for button animation
        myImageButton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    myImageButton.setBackgroundResource(R.drawable.btn_letsgo_pressed)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    myImageButton.setBackgroundResource(R.drawable.btn_letsgo)
                }
            }
            false
        }

        myImageButton.setOnClickListener {
            // Intent to transition to StudentMainActivity
            val intent = Intent(this, StudentMainActivity::class.java)
            startActivity(intent)
            //finish() // Finish the splash activity
        }
    }

    override fun onPause() {
        super.onPause()
        // Pause music if needed
        val isMusicEnabled = sharedPreferences.getBoolean("MusicEnabled", true)
        if (isMusicEnabled) {
            //stopService(Intent(this, BackgroundMusicService::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Resume music if needed
        val isMusicEnabled = sharedPreferences.getBoolean("MusicEnabled", true)
        if (isMusicEnabled) {
            startService(Intent(this, BackgroundMusicService::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop music service to release resources
        stopService(Intent(this, BackgroundMusicService::class.java))
    }
}
