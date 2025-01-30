package com.example.playlistmaker.presentation.playlists.models

import com.example.playlistmaker.domain.playlists.models.Playlist

interface PlaylistsScreenState {
    data object Empty : PlaylistsScreenState
    data class Content(val playlists: List<Playlist>) : PlaylistsScreenState
}