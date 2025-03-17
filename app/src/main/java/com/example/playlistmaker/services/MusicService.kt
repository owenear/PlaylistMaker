package com.example.playlistmaker.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.activity.PlayerFragment.Companion.ARGS_TRACK
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
    private var track: Track? = null

    private val _playerState = MutableStateFlow<PlayerScreenState>(PlayerScreenState.Default)
    private val playerState = _playerState.asStateFlow()

    private var playTimeJob: Job? = null
    private val mediaPlayerInteractor: MediaPlayerInteractor by inject()

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder {
        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getSerializableExtra(ARGS_TRACK, Track::class.java)
        } else {
            intent?.getSerializableExtra(ARGS_TRACK) as Track
        }
        initPlayer()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
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

    override fun startNotification() {
        ServiceCompat.startForeground(this, SERVICE_NOTIFICATION_ID,
            createServiceNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
    }

    override fun stopNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun initPlayer() {
        mediaPlayerInteractor.preparePlayer(track!!.previewUrl,
            { onPreparedListener() }, { onCompletionListener() })
    }

    private fun onPreparedListener(){
        _playerState.value = PlayerScreenState.Prepared
    }

    private fun onCompletionListener(){
        playTimeJob?.cancel()
        stopNotification()
        _playerState.value = PlayerScreenState.Prepared
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
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = NOTIFICATION_CHANNEL_DESCR
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("${track?.artistName} - ${track?.trackName}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    override fun getPlayerState(): StateFlow<PlayerScreenState> {
        return playerState
    }

    override fun onDestroy() {
        playTimeJob?.cancel()
        mediaPlayerInteractor.releasePlayer()
        super.onDestroy()
    }

    private companion object {
        const val NOTIFICATION_CHANNEL_NAME = "Music service"
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val NOTIFICATION_CHANNEL_DESCR = "Service for playing music"
        const val SERVICE_NOTIFICATION_ID = 100
        private const val PLAY_TIME_DELAY = 500L
    }

}