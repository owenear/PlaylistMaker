package com.example.playlistmaker.domain.playlists.api

import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun removePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

}