package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.SearchRequest
import com.example.playlistmaker.data.dto.SearchResponse
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl (private val networkClient: NetworkClient) : TrackRepository {

	override fun searchTrack(query: String): List<Track>? {
		val response = networkClient.doRequest(SearchRequest(query))
		if (response.resultCode == 200) {
			return (response as SearchResponse).results.map {
				Track(  it.trackId,
					it.trackName,
					it.artistName,
					it.getFormatTrackTime("mm:ss"),
					it.artworkUrl100,
					it.previewUrl,
					it.collectionName,
					it.getReleaseYear(),
					it.primaryGenreName,
					it.country  )
			}
		} else {
			return null
		}
	}

}