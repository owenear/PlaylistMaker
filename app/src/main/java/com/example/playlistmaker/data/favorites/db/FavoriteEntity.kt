package com.example.playlistmaker.data.favorites.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "track_id")
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val trackTimeFormat: String,
    val artworkUrl100: String?,
    val previewUrl: String,
    val collectionName: String?,
    val releaseYear: String?,
    val primaryGenreName: String?,
    val country: String?
)
