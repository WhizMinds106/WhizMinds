package dev.hy.whizminds

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val CHANNEL_ID = "MusicServiceChannel"

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val isMusicEnabled = sharedPreferences.getBoolean("music_enabled", true)
        val volumeLevel = sharedPreferences.getFloat("music_volume", 1.0f)

        if (isMusicEnabled) {
            // Initialize MediaPlayer
            mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
            mediaPlayer.isLooping = true
            mediaPlayer.setVolume(volumeLevel, volumeLevel)

            // Start foreground service with notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            }

            val notificationIntent = Intent(this, SplashActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
            val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Service")
                .setContentText("Playing background music")
                .setSmallIcon(R.drawable.note)
                .setContentIntent(pendingIntent)
                .build()

            startForeground(1, notification)

            // Start playing music
            mediaPlayer.start()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        // Release the media player resources
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        // No binding provided
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}