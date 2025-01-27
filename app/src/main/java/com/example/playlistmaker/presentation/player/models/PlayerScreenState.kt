package com.example.playlistmaker.presentation.player.models

import com.example.playlistmaker.domain.playlists.models.Playlist

interface PlayerScreenState {
	data object Default : PlayerScreenState
	data object Prepared : PlayerScreenState
	data class Playing(val playTime: Int) : PlayerScreenState
	data class Paused(val pauseTime: Int) : PlayerScreenState
	data class Favorite(val isFavorite: Boolean) : PlayerScreenState
	data class Playlists(val playlists: List<Playlist>) : PlayerScreenState
	data class AddResult(val isTrackInPlaylist: Boolean, val playlist: Playlist) : PlayerScreenState
}