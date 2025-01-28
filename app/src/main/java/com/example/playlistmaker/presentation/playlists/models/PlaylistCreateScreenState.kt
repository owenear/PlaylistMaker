package com.example.playlistmaker.presentation.playlists.models

interface PlaylistCreateScreenState {

    data object Disabled : PlaylistCreateScreenState
    data object Enabled : PlaylistCreateScreenState
    data class Created(val playlistName: String) : PlaylistCreateScreenState
    data class BackPressed(val isPlaylistCreated: Boolean) : PlaylistCreateScreenState

}