package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.Resource

interface TrackInteractor {

	fun searchTracks(query: String, consumer: TrackConsumer)

	interface TrackConsumer {
		fun consume(found: Resource<List<Track>>)
	}

}