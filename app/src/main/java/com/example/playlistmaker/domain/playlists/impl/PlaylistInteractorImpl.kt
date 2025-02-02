package com.example.playlistmaker.domain.playlists.impl

import com.example.playlistmaker.domain.playlists.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.playlists.models.Playlist
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

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override fun getPlaylist(playlist: Playlist): Flow<Playlist> {
        return playlistRepository.getPlaylist(playlist)
    }


    override fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>> {
        return playlistRepository.getTracksInPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.deleteTrackFromPlaylist(track, playlist)
    }

}