package com.example.playlistmaker.data.playlists

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.data.FileStorage
import com.example.playlistmaker.domain.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.mappers.PlaylistMapper
import com.example.playlistmaker.util.mappers.TrackMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(private val appDatabase: AppDatabase,
                             private val playlistMapper: PlaylistMapper,
                             private val trackMapper: TrackMapper,
                             private val fileStorage: FileStorage) : PlaylistRepository{

    override suspend fun createPlaylist(playlist: Playlist) {
        playlist.id = appDatabase.playlistDao().getLastPlaylistId() + 1
        playlist.coverUri = fileStorage.saveData(playlistMapper.map(playlist))
        appDatabase.playlistDao().insertPlaylist(playlistMapper.map(playlist))
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlistMapper.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylistsWithTracks().map {
            playlistsWithTracks -> playlistMapper.map(playlistsWithTracks.playlist).apply {
                trackCount = playlistsWithTracks.tracks.count()
            }
        }
        emit(playlists)
    }

    override fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>> = flow {
        val trackEntityList = appDatabase.playlistDao().getPlaylistWithTracks(playlist.id!!).tracks
        emit(trackEntityList.map { trackEntity -> trackMapper.mapEntity(trackEntity)})
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        appDatabase.playlistDao().addTrackToPlaylist(trackMapper.mapEntity(track),
            playlistMapper.map(playlist))
    }

}