package com.example.playlistmaker.data.playlists.dto

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackCrossRefEntity(
    val playlistId: Int,
    val trackId: Int
)
