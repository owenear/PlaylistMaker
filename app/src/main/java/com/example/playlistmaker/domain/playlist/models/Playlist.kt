package com.example.playlistmaker.domain.playlist.models

data class Playlist(
    var id: Int?,
    val name: String,
    val description: String?,
    var coverUri: String?
)
