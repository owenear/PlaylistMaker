package com.example.playlistmaker.util.mappers

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.data.search.dto.TrackHistoryDto
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TrackHistory

class TrackHistoryMapper {

    fun map(trackHistoryDto: TrackHistoryDto): TrackHistory {
        val trackHistoryList = trackHistoryDto.trackList.map {
            Track (
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTime,
                it.trackTimeFormat,
                it.artworkUrl100,
                it.previewUrl ?: "",
                it.collectionName,
                it.releaseYear,
                it.primaryGenreName,
                it.country
            )
        }
        return TrackHistory(trackHistoryList.toMutableList())
    }

    fun map (trackHistory: TrackHistory): TrackHistoryDto {
        val trackHistoryList = trackHistory.trackList.map {
            TrackDto (
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTime,
                it.artworkUrl100,
                it.previewUrl,
                it.collectionName,
                it.releaseYear,
                it.primaryGenreName,
                it.country,
                it.releaseYear,
                it.trackTimeFormat
            )
        }
        return TrackHistoryDto(trackHistoryList)
    }

}