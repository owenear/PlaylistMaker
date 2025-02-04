package com.example.playlistmaker.presentation.playlists.models

import com.example.playlistmaker.domain.playlists.models.Playlist

interface PlaylistCreateScreenState {

    data object Create : PlaylistCreateScreenState
    data class Update(val playlist: Playlist) : PlaylistCreateScreenState
    data object Disabled : PlaylistCreateScreenState
    data object Enabled : PlaylistCreateScreenState
    data class Created(val playlistName: String) : PlaylistCreateScreenState
    data class Updated(val playlistName: String) : PlaylistCreateScreenState
    data class BackPressed(val isPlaylistCreated: Boolean) : PlaylistCreateScreenState

}