package com.example.playlistmaker.data.playlists.dto

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackCrossRef(
    val playlistId: Int,
    val trackId: Int
)
