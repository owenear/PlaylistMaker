package com.example.playlistmaker.domain.playlists.models

import android.net.Uri
import java.io.Serializable

data class Playlist(
    var id: Int?,
    val name: String,
    val description: String?,
    var coverUri: Uri?,
    var trackCount: Int = 0,
    var duration: Int = 0,
    var durationFormat: String = "0"
) : Serializable
