package com.example.playlistmaker.util.mappers
import com.example.playlistmaker.data.favorites.db.FavoriteEntity
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.models.Track

class TrackMapper {

    fun map(trackDto: TrackDto): Track {
        return 	Track(
            trackDto.trackId,
            trackDto.trackName,
            trackDto.artistName,
            trackDto.getFormatTrackTime("mm:ss"),
            trackDto.artworkUrl100,
            trackDto.previewUrl ?: "",
            trackDto.collectionName,
            trackDto.getYearRelease(),
            trackDto.primaryGenreName,
            trackDto.country,
        )
    }

    fun map(track: Track): FavoriteEntity {
        return FavoriteEntity(
            null,
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeFormat,
            track.artworkUrl100,
            track.previewUrl,
            track.collectionName,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
        )
    }

    fun map(trackEntity: FavoriteEntity): Track {
        return Track(
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeFormat,
            trackEntity.artworkUrl100,
            trackEntity.previewUrl,
            trackEntity.collectionName,
            trackEntity.releaseYear,
            trackEntity.primaryGenreName,
            trackEntity.country,
            true
        )
    }


}