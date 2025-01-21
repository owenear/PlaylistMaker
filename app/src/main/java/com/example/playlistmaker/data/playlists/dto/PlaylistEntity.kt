package com.example.playlistmaker.data.playlists.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Int?,
    val name: String,
    val description: String?,
    val coverUri: String?,
    val trackIds: String = "",
    val trackCount: Int = 0,
)