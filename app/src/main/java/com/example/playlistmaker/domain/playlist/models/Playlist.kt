package com.example.playlistmaker.domain.playlist.models

import android.net.Uri

data class Playlist(
    var id: Int?,
    val name: String,
    val description: String?,
    var coverUri: Uri?,
    var trackCount: Int = 0
)
