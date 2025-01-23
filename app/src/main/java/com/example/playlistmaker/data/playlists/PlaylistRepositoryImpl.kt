package com.example.playlistmaker.data.playlists

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.domain.playlist.api.PlaylistRepository
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.util.mappers.PlaylistMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(private val appDatabase: AppDatabase,
                             private val playlistMapper: PlaylistMapper) : PlaylistRepository{

    override suspend fun createPlaylist(playlist: Playlist) {
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


}