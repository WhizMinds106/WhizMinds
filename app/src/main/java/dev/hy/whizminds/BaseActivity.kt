package dev.hy.whizminds

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    private lateinit var musicIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the music service intent
        musicIntent = Intent(this, MusicService::class.java)

        // Start the background music
        startService(musicIntent)
    }

    override fun onPause() {
        super.onPause()
        stopService(musicIntent)
    }

    override fun onResume() {
        super.onResume()
        startService(musicIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(musicIntent)
    }
}