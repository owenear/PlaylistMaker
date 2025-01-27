package com.example.playlistmaker.domain.playlist.impl

import com.example.playlistmaker.domain.playlist.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.api.PlaylistRepository
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository)
    : PlaylistInteractor {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        playlistRepository.removePlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun isTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        TODO("Not yet implemented")
    }

}