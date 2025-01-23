package com.example.playlistmaker.domain.playlist.models

data class Playlist(
    val id: Int?,
    val name: String,
    val description: String?,
    val coverUri: String?,
)
