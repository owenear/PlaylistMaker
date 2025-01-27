package com.example.playlistmaker.util.mappers

import androidx.core.net.toUri
import com.example.playlistmaker.data.playlists.dto.PlaylistEntity
import com.example.playlistmaker.domain.playlists.models.Playlist

class PlaylistMapper {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverUri?.toString()
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.playlistId,
            playlistEntity.name,
            playlistEntity.description,
            playlistEntity.coverUri?.toUri()
        )
    }
}