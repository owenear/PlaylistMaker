package com.example.playlistmaker.presentation.playlist.model

import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track

interface PlaylistScreenState {
    data class Init(val playlist: Playlist) : PlaylistScreenState
    data class Content(val tracks: List<Track>) : PlaylistScreenState
}
