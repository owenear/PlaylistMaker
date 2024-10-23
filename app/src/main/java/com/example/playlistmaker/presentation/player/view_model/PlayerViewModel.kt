package com.example.playlistmaker.presentation.player.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.presentation.player.models.PlayerScreenState
import com.example.playlistmaker.util.Creator

class PlayerViewModel(private val previewUrl: String,
	private val mediaPlayerInteractor: MediaPlayerInteractor): ViewModel() {

	private val stateMutableLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Default)
	val stateLiveData : LiveData<PlayerScreenState> = stateMutableLiveData

	private val playTimeMutableLiveData = MutableLiveData<Int>()
	val playTimeLiveData : LiveData<Int> = playTimeMutableLiveData

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
		playTimeMutableLiveData.postValue(0)
		renderState(PlayerScreenState.Prepared)
	}

	fun startPlayer() {
		mediaPlayerInteractor.startPlayer()
		handler.postAtTime(updatePlayTime(),PLAYER_REQUEST_TOKEN,
			SystemClock.uptimeMillis() + PLAY_TIME_DELAY
		)
		renderState(PlayerScreenState.Playing)
	}

	fun pausePlayer() {
		if (stateMutableLiveData.value == PlayerScreenState.Playing) {
			mediaPlayerInteractor.pausePlayer()
			handler.removeCallbacksAndMessages(PLAYER_REQUEST_TOKEN)
			renderState(PlayerScreenState.Paused)
		}
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

	private fun updatePlayTime() : Runnable = Runnable {
		handler.postAtTime(updatePlayTime(),PLAYER_REQUEST_TOKEN,
			SystemClock.uptimeMillis() + PLAY_TIME_DELAY
		)
		playTimeMutableLiveData.postValue(mediaPlayerInteractor.getPlayTime())
	}

	companion object {
		fun getViewModelFactory(previewUrl: String): ViewModelProvider.Factory = viewModelFactory {
			initializer {
				PlayerViewModel(previewUrl, Creator.provideMediaPlayerInteractor())
			}
		}
		private const val PLAY_TIME_DELAY = 500L
		private val PLAYER_REQUEST_TOKEN = Any()
	}

}