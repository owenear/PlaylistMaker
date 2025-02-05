package com.example.playlistmaker.util.mappers
import com.example.playlistmaker.data.favorites.dto.FavoriteEntity
import com.example.playlistmaker.data.playlists.dto.TrackEntity
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.models.Track

class TrackMapper {

    fun map(trackDto: TrackDto): Track {
        return 	Track(
            trackDto.trackId,
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTime,
            trackDto.getFormatTrackTime(),
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
            track.trackTime,
            track.trackTimeFormat,
            track.artworkUrl100,
            track.previewUrl,
            track.collectionName,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
        )
    }

    fun map(favoriteEntity: FavoriteEntity): Track {
        return Track(
            favoriteEntity.itunesId,
            favoriteEntity.trackName,
            favoriteEntity.artistName,
            favoriteEntity.trackTime,
            favoriteEntity.trackTimeFormat,
            favoriteEntity.artworkUrl100,
            favoriteEntity.previewUrl,
            favoriteEntity.collectionName,
            favoriteEntity.releaseYear,
            favoriteEntity.primaryGenreName,
            favoriteEntity.country,
            true
        )
    }

    fun mapEntity(trackEntity: TrackEntity): Track {
        return Track(
            trackEntity.itunesId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTime,
            trackEntity.trackTimeFormat,
            trackEntity.artworkUrl100,
            trackEntity.previewUrl,
            trackEntity.collectionName,
            trackEntity.releaseYear,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.isFavorite
        )
    }

    fun mapEntity(track: Track): TrackEntity {
        return TrackEntity(
            null,
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.trackTimeFormat,
            track.artworkUrl100,
            track.previewUrl,
            track.collectionName,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
            track.isFavorite
        )
    }

}