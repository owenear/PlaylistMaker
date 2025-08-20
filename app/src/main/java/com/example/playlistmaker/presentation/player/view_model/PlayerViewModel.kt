package com.example.playlistmaker.presentation.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorites.api.FavoriteInteractor
import com.example.playlistmaker.domain.playlists.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.models.PlayerScreenState
import com.example.playlistmaker.services.AudioPlayerControl
import kotlinx.coroutines.launch

class PlayerViewModel(private val track: Track,
	private val favoriteInteractor: FavoriteInteractor,
	private val playlistInteractor: PlaylistInteractor
	): ViewModel() {

	private val stateMutableLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Default)
	val stateLiveData : LiveData<PlayerScreenState> = stateMutableLiveData

	private var audioPlayerControl: AudioPlayerControl? = null

	init {
		updateData()
	}

	fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
		this.audioPlayerControl = audioPlayerControl
		viewModelScope.launch {
			audioPlayerControl.getPlayerState().collect {
				renderState(it)
			}
		}
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

	fun playbackControl(onActivityPause: Boolean = false) {
		when(stateMutableLiveData.value) {
			is PlayerScreenState.Playing -> audioPlayerControl?.pausePlayer()
			else -> if (!onActivityPause) audioPlayerControl?.startPlayer()
		}
	}

	fun removeAudioPlayerControl() {
		audioPlayerControl = null
	}

	fun startNotification(){
		if (stateMutableLiveData.value is PlayerScreenState.Playing)
			audioPlayerControl?.startNotification()
	}

	fun stopNotification(){
		if (stateMutableLiveData.value is PlayerScreenState.Playing)
			audioPlayerControl?.stopNotification()
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
			if (playlistInteractor.isTrackInPlaylist(playlist.id!!, track.trackId)) {
				renderState(PlayerScreenState.AddResult(true, playlist))
			} else {
				playlistInteractor.addTrackToPlaylist(track, playlist)
				renderState(PlayerScreenState.AddResult(false, playlist))
			}
		}
	}

	private fun renderState(state: PlayerScreenState) {
		stateMutableLiveData.postValue(state)
	}

	override fun onCleared() {
		removeAudioPlayerControl()
		super.onCleared()
	}

}