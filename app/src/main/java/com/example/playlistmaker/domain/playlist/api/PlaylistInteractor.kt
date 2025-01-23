package com.example.playlistmaker.domain.playlist.api

import com.example.playlistmaker.domain.playlist.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun removePlaylist(playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

}