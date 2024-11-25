package com.example.playlistmaker.presentation.player.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.presentation.player.models.PlayerScreenState

class PlayerViewModel(private val previewUrl: String,
	private val mediaPlayerInteractor: MediaPlayerInteractor): ViewModel() {

	private val stateMutableLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Default)
	val stateLiveData : LiveData<PlayerScreenState> = stateMutableLiveData

	private val handler = Handler(Looper.getMainLooper())

	init {
		mediaPlayerInteractor.preparePlayer(previewUrl,
			{ onPreparedListener() }, { onCompletionListener() })
	}

	private fun onPreparedListener(){
		renderState(PlayerScreenState.Prepared)
	}

	private fun onCompletionListener(){
		handler.removeCallbacksAndMessages(PLAYER_REQUEST_TOKEN)
		renderState(PlayerScreenState.Prepared)
	}

	private fun startPlayer() {
		mediaPlayerInteractor.startPlayer()
		updatePlayTime()
	}

	fun pausePlayer() {
		mediaPlayerInteractor.pausePlayer()
		handler.removeCallbacksAndMessages(PLAYER_REQUEST_TOKEN)
		if (stateMutableLiveData.value is PlayerScreenState.Playing)
			renderState(PlayerScreenState.Paused(mediaPlayerInteractor.getPlayTime()))
	}

	fun playbackControl() {
		when(stateMutableLiveData.value) {
			is PlayerScreenState.Playing -> {
				pausePlayer()
			}
			else -> {
				startPlayer()
			}
		}
	}

	private fun renderState(state: PlayerScreenState) {
		stateMutableLiveData.postValue(state)
	}

	override fun onCleared() {
		handler.removeCallbacksAndMessages(PLAYER_REQUEST_TOKEN)
		mediaPlayerInteractor.releasePlayer()
		super.onCleared()
	}

	private fun updatePlayTime() {
		handler.postAtTime( { updatePlayTime() },PLAYER_REQUEST_TOKEN,
			SystemClock.uptimeMillis() + PLAY_TIME_DELAY
		)
		renderState(PlayerScreenState.Playing(mediaPlayerInteractor.getPlayTime()))
	}

	companion object {
		private const val PLAY_TIME_DELAY = 500L
		private val PLAYER_REQUEST_TOKEN = Any()
	}

}