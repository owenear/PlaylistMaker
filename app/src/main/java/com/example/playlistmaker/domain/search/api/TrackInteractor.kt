package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track

interface TrackInteractor {

	fun searchTracks(query: String, consumer: TrackConsumer)

	interface TrackConsumer {
		fun consume(foundTrack: List<Track>?)
	}

}