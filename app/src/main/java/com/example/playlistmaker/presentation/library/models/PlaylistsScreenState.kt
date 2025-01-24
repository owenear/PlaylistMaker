package com.example.playlistmaker.presentation.library.models

import com.example.playlistmaker.domain.playlist.models.Playlist

interface PlaylistsScreenState {
    data object Empty : PlaylistsScreenState
    data class Content(val playlists: List<Playlist>) : PlaylistsScreenState
}