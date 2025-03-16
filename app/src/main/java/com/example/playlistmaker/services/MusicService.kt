package com.example.playlistmaker.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.presentation.player.models.PlayerScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MusicService: Service(), AudioPlayerControl {

    private val binder = MusicServiceBinder()
    private var trackUrl: String? = null

    private val _playerState = MutableStateFlow<PlayerScreenState>(PlayerScreenState.Default)
    private val playerState = _playerState.asStateFlow()

    private var playTimeJob: Job? = null
    private val mediaPlayerInteractor: MediaPlayerInteractor by inject()

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder {
        trackUrl = intent?.getStringExtra("track_url") ?: ""
        initMediaPlayer()
        ServiceCompat.startForeground(
            /* service = */ this,
            /* id = */ SERVICE_NOTIFICATION_ID,
            /* notification = */ createServiceNotification(),
            /* foregroundServiceType = */ ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        playTimeJob?.cancel()
        mediaPlayerInteractor.releasePlayer()
        super.onDestroy()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate")
        createNotificationChannel()
    }

    override fun getPlayerState(): StateFlow<PlayerScreenState> {
        return playerState
    }

    private fun initMediaPlayer() {
        mediaPlayerInteractor.preparePlayer(trackUrl!!,
            { onPreparedListener() }, { onCompletionListener() })
    }

    private fun onPreparedListener(){
        _playerState.value = PlayerScreenState.Prepared
    }

    private fun onCompletionListener(){
        playTimeJob?.cancel()
        _playerState.value = PlayerScreenState.Prepared
    }

    override fun pausePlayer() {
        playTimeJob?.cancel()
        mediaPlayerInteractor.pausePlayer()
        _playerState.value = PlayerScreenState.Paused(mediaPlayerInteractor.getPlayTime())
    }

    override fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        updatePlayTime()
    }

    private fun updatePlayTime() {
        playTimeJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                _playerState.value = PlayerScreenState.Playing(mediaPlayerInteractor.getPlayTime())
                delay(PLAY_TIME_DELAY)
            }
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            /* id= */ NOTIFICATION_CHANNEL_ID,
            /* name= */ "Music service",
            /* importance= */ NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Music foreground service")
            .setContentText("Our service is working right now!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private companion object {
        const val LOG_TAG = "MusicService"
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val SERVICE_NOTIFICATION_ID = 100
        private const val PLAY_TIME_DELAY = 500L
    }

}