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
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT playlistId FROM playlists ORDER BY playlistId DESC LIMIT 1")
    suspend fun getLastPlaylistId(): Int

    @Query("SELECT * FROM playlists ORDER BY playlistId DESC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Transaction
    @Query("SELECT * FROM playlists")
    fun getPlaylistsWithTracks(): List<PlaylistWithTracks>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(playlistTrackCrossRef: PlaylistTrackCrossRefEntity)

    @Transaction
    suspend fun addTrackToPlaylist(trackEntity: TrackEntity, playlistEntity: PlaylistEntity) {
        insertTrack(trackEntity)
        insertPlaylistTrack(PlaylistTrackCrossRefEntity(playlistEntity.playlistId!!,
            trackEntity.trackId!!))
    }




}