package com.example.playlistmaker.data.playlists.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val trackTimeFormat: String,
    val artworkUrl100: String?,
    val previewUrl: String,
    val collectionName: String?,
    val releaseYear: String?,
    val primaryGenreName: String?,
    val country: String?,
    val isFavorite: Boolean = false
)
