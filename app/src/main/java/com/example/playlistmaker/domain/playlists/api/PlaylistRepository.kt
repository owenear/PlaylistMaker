package com.example.playlistmaker.domain.playlists.api

import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun removePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylist(playlist: Playlist): Flow<Playlist>

    fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>>

    suspend fun getTrackInPlaylistById(playlistId: Int, trackId: Int): Track?

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist)

}