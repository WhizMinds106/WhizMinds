package dev.hy.whizminds

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import pl.droidsonroids.gif.GifImageButton

class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)

        // Resume music
        MusicManager.resumeMusic()

        // Button Reference
        val buttonStudent = findViewById<Button>(R.id.btn_student)
        val buttonTeacher = findViewById<GifImageButton>(R.id.btn_teacher)

        // Action for student button
        buttonStudent.setOnClickListener {
            val intent = Intent(this, StudentMainActivity::class.java)
            startActivity(intent)
            finish()
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

    override fun onPause() {
        super.onPause()
        // When the activity is paused (e.g., when minimized)
        MusicManager.shouldPauseMusic = true
        MusicManager.pauseMusic()  // Ensure music pauses when app is minimized
    }

    override fun onStop() {
        super.onStop()
        // Stop music here as well to cover all lifecycle states
        MusicManager.pauseMusic()  // Ensures music pauses when fully stopped
    }

    override fun onResume() {
        super.onResume()
        // Resume music only if needed
        MusicManager.resumeMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Optionally stop music completely if the activity is destroyed
        MusicManager.pauseMusic()
    }
}