package dev.hy.whizminds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {

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

        // Content view
        setContentView(R.layout.activity_splash)

        // Initialize and start music
        MusicManager.initialize(this)

        // Button reference
        val myImageButton = findViewById<ImageButton>(R.id.btn_letsgo)

        // Logic for button animation
        myImageButton.setOnTouchListener { v, event ->
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
            // Disable music pause during transition
            MusicManager.shouldPauseMusic = false
            val intent = Intent(this, StudentMainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pauseMusic()
    }

    override fun onResume() {
        super.onResume()
        MusicManager.resumeMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.pauseMusic()
    }
}