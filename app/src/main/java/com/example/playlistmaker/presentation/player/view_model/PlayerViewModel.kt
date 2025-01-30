package com.example.playlistmaker.presentation.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorites.api.FavoriteInteractor
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.playlists.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.models.PlayerScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val track: Track,
	private val mediaPlayerInteractor: MediaPlayerInteractor,
	private val favoriteInteractor: FavoriteInteractor,
	private val playlistInteractor: PlaylistInteractor
	): ViewModel() {

	private val stateMutableLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Default)
	val stateLiveData : LiveData<PlayerScreenState> = stateMutableLiveData
	private var playTimeJob: Job? = null

	init {
		updateData()
		mediaPlayerInteractor.preparePlayer(track.previewUrl,
			{ onPreparedListener() }, { onCompletionListener() })
	}

	fun updateData() {
		viewModelScope.launch {
			track.isFavorite = favoriteInteractor.isFavorite(track)
			renderState(PlayerScreenState.Favorite(track.isFavorite))
			getPlaylists()
		}
	}

	private suspend fun getPlaylists() {
		playlistInteractor.getPlaylists().collect { playlists ->
			renderState(PlayerScreenState.Playlists(playlists))
		}
	}

	private fun onPreparedListener(){
		renderState(PlayerScreenState.Prepared)
	}

	private fun onCompletionListener(){
		playTimeJob?.cancel()
		renderState(PlayerScreenState.Prepared)
	}

	private fun startPlayer() {
		mediaPlayerInteractor.startPlayer()
		updatePlayTime()
	}

	private fun pausePlayer() {
		playTimeJob?.cancel()
		mediaPlayerInteractor.pausePlayer()
		renderState(PlayerScreenState.Paused(mediaPlayerInteractor.getPlayTime()))
	}

	fun playbackControl(onActivityPause: Boolean = false) {
		when(stateMutableLiveData.value) {
			is PlayerScreenState.Playing -> pausePlayer()
			else -> if (!onActivityPause) startPlayer()
		}
	}

	private fun renderState(state: PlayerScreenState) {
		stateMutableLiveData.postValue(state)
	}

	override fun onCleared() {
		mediaPlayerInteractor.releasePlayer()
		super.onCleared()
	}

	private fun updatePlayTime() {
		playTimeJob = viewModelScope.launch {
			while (true) {
				renderState(PlayerScreenState.Playing(mediaPlayerInteractor.getPlayTime()))
				delay(PLAY_TIME_DELAY)
			}
		}
	}

	fun onFavoriteClicked() {
		if (track.isFavorite) {
			viewModelScope.launch {
				favoriteInteractor.removeFromFavorites(track)
			}
			track.isFavorite = false
		}
		else {
			viewModelScope.launch {
				track.isFavorite = true
				favoriteInteractor.addToFavorites(track)
			}
		}
		renderState(PlayerScreenState.Favorite(track.isFavorite))
	}

	fun onPlaylistClicked(playlist: Playlist) {
		viewModelScope.launch {
			playlistInteractor.getTracksInPlaylist(playlist).collect { tracks ->
					if (track in tracks) {
						renderState(PlayerScreenState.AddResult(true, playlist))
					} else {
						playlistInteractor.addTrackToPlaylist(track, playlist)
						renderState(PlayerScreenState.AddResult(false, playlist))
					}
			}
		}
	}

	companion object {
		private const val PLAY_TIME_DELAY = 500L
	}

}