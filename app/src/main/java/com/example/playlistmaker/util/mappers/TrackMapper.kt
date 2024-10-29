package com.example.playlistmaker.util.mappers
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
            trackDto.previewUrl,
            trackDto.collectionName,
            trackDto.getYearRelease(),
            trackDto.primaryGenreName,
            trackDto.country
        )
    }

}