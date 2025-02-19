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
        if (playlist.id == null)
            playlist.id = appDatabase.playlistDao().getLastPlaylistId() + 1
        playlist.coverUri = fileStorage.saveData(playlistMapper.map(playlist)).toString()
        appDatabase.playlistDao().insertPlaylist(playlistMapper.map(playlist))
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().removePlaylist(playlistMapper.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylistsWithTracks().map {
            playlistsWithTracks -> playlistMapper.map(playlistsWithTracks.playlist).apply {
                trackCount = playlistsWithTracks.tracks.count()
                duration = playlistsWithTracks.tracks.sumOf { it.trackTime }
                durationFormat = (duration/60000).toString()
            }
        }
        emit(playlists)
    }

    override fun getPlaylist(playlist: Playlist): Flow<Playlist> = flow {
        val playlistWithTracks = appDatabase.playlistDao().getPlaylistWithTracks(playlist.id!!)
        emit(playlistMapper.map(playlistWithTracks.playlist).apply {
            trackCount = playlistWithTracks.tracks.count()
            duration = playlistWithTracks.tracks.sumOf { it.trackTime }
            durationFormat = (duration/60000).toString()
        })
    }

    override fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>> = flow {
        val trackEntityList = appDatabase.playlistDao().getTracksInPlaylist(playlist.id!!)
        emit(trackEntityList.map { trackEntity -> trackMapper.mapEntity(trackEntity)})
    }

    override suspend fun getTrackInPlaylistById(playlistId: Int, trackId: Int): Track? {
        val trackEntity = appDatabase.playlistDao().getTrackInPlaylistById(playlistId, trackId)
        return if (trackEntity != null) trackMapper.mapEntity(trackEntity) else null
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        appDatabase.playlistDao().addTrackToPlaylist(trackMapper.mapEntity(track),
            playlistMapper.map(playlist))
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {
        appDatabase.playlistDao().deleteTrackFromPlaylist(trackMapper.mapEntity(track),
            playlistMapper.map(playlist))

    }

}