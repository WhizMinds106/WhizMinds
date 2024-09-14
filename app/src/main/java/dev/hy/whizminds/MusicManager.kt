package dev.hy.whizminds

import android.content.Context
import android.health.connect.datatypes.units.Volume
import android.media.MediaPlayer
import android.preference.PreferenceManager

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isInitialized = false
    var shouldPauseMusic = true

    fun initialize(context: Context) {
        if (!isInitialized) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val isMusicEnabled = sharedPreferences.getBoolean("music_enabled", true)
            val volumeLevel = sharedPreferences.getFloat("music_volume", 1.0f)

            if (isMusicEnabled) {
                mediaPlayer = MediaPlayer.create(context, R.raw.background_music).apply {
                    isLooping = true
                    setVolume(volumeLevel, volumeLevel)
                    start()
                }
            }

            isInitialized = true
        }
    }

    fun setVolume(context: Context, volume: Float) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putFloat("music_volume", volume).apply()
        mediaPlayer?.setVolume(volume, volume)
    }

    fun setMusicEnabled(context: Context, enabled: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putBoolean("music_enabled", enabled).apply()

        if (enabled) {
            initialize(context)
        } else {
            stopMusic()
        }
    }

    fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isInitialized = false
    }

    fun pauseMusic() {
        if (shouldPauseMusic) {
            mediaPlayer?.pause()
        }
    }

    fun resumeMusic() {
        mediaPlayer?.start()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}