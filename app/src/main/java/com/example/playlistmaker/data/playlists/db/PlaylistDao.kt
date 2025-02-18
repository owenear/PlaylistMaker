package com.example.playlistmaker.data.playlists.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.playlists.dto.PlaylistEntity
import com.example.playlistmaker.data.playlists.dto.PlaylistTrackCrossRefEntity
import com.example.playlistmaker.data.playlists.dto.PlaylistWithTracks
import com.example.playlistmaker.data.playlists.dto.TrackEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT playlistId FROM playlists ORDER BY playlistId DESC LIMIT 1")
    suspend fun getLastPlaylistId(): Int

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :playlistId")
    suspend fun getPlaylistWithTracks(playlistId: Int): PlaylistWithTracks

    @Transaction
    @Query("SELECT * FROM playlists")
    suspend fun getPlaylistsWithTracks(): List<PlaylistWithTracks>

    @Query("SELECT * FROM tracks t JOIN playlists_tracks pt ON t.itunesId = pt.itunesId " +
            "JOIN playlists p ON p.playlistId = pt.playlistId WHERE p.playlistId = :playlistId " +
            "ORDER BY t.trackId DESC")
    suspend fun getTracksInPlaylist(playlistId: Int): List<TrackEntity>

    @Query("SELECT * FROM tracks t JOIN playlists_tracks pt ON t.itunesId = pt.itunesId " +
            "JOIN playlists p ON p.playlistId = pt.playlistId WHERE p.playlistId = :playlistId " +
            "and t.itunesId = :itunesId")
    suspend fun getTrackInPlaylistById(playlistId: Int, itunesId: Int): TrackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM tracks WHERE itunesId = :itunesId")
    suspend fun getTrackByID(itunesId: Int): TrackEntity?

    @Query("DELETE FROM tracks where itunesId not in (select itunesId from playlists_tracks)")
    suspend fun deleteTracksNoPlaylist()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(playlistTrackCrossRef: PlaylistTrackCrossRefEntity)

    @Delete
    suspend fun deletePlaylistTrack(playlistTrackCrossRef: PlaylistTrackCrossRefEntity)

    @Query("DELETE FROM playlists_tracks where playlistId = :playlistId")
    suspend fun deletePlaylistTracks(playlistId: Int)

    @Transaction
    suspend fun addTrackToPlaylist(trackEntity: TrackEntity, playlistEntity: PlaylistEntity) {
        if (getTrackByID(trackEntity.itunesId) == null)
            insertTrack(trackEntity)
        insertPlaylistTrack(PlaylistTrackCrossRefEntity(playlistEntity.playlistId!!,
            trackEntity.itunesId))
    }

    @Transaction
    suspend fun deleteTrackFromPlaylist(trackEntity: TrackEntity, playlistEntity: PlaylistEntity) {
        deletePlaylistTrack(PlaylistTrackCrossRefEntity(playlistEntity.playlistId!!,
            trackEntity.itunesId))
        deleteTracksNoPlaylist()
    }

    @Transaction
    suspend fun removePlaylist(playlistEntity: PlaylistEntity){
        deletePlaylistTracks(playlistEntity.playlistId!!)
        deletePlaylist(playlistEntity)
        deleteTracksNoPlaylist()
    }

}