package com.example.playlistmaker.data.playlists.dto

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "itunesId"], tableName = "playlists_tracks")
data class PlaylistTrackCrossRefEntity(
    val playlistId: Int,
    val itunesId: Int
)
