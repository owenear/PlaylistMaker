package com.example.playlistmaker.data.playlists.dto

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "itunesId"], tableName = "playlists_tracks")
data class PlaylistTrackCrossRefEntity(
    @ColumnInfo(index = true)
    val playlistId: Int,
    @ColumnInfo(index = true)
    val itunesId: Int
)
