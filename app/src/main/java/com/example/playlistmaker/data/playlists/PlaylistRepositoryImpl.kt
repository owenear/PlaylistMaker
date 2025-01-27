package com.example.playlistmaker.data.playlists

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.FileStorage
import com.example.playlistmaker.domain.playlist.api.PlaylistRepository
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.mappers.PlaylistMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(private val appDatabase: AppDatabase,
                             private val playlistMapper: PlaylistMapper,
                             private val fileStorage: FileStorage) : PlaylistRepository{

    override suspend fun createPlaylist(playlist: Playlist) {
        playlist.id = appDatabase.playlistDao().getLastPlaylistId() + 1
        playlist.coverUri = fileStorage.saveData(playlistMapper.map(playlist))
        appDatabase.playlistDao().insertPlaylist(playlistMapper.map(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists().map {
                playlists -> playlists.map { playlist -> playlistMapper.map(playlist) }
        }
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlistMapper.map(playlist))
    }

    override suspend fun isTrackInPlaylist(track: Track, playlist: Playlist): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        TODO("Not yet implemented")
    }

}