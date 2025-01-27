package com.example.playlistmaker.domain.playlist.api

import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun removePlaylist(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun isTrackInPlaylist(track: Track, playlist: Playlist): Boolean

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

}