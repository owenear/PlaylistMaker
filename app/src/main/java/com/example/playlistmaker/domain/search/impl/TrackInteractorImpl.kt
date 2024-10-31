package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.TrackInteractor
import com.example.playlistmaker.domain.search.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val trackRepository: TrackRepository) : TrackInteractor {

	private val executor = Executors.newCachedThreadPool()

	override fun searchTracks(query: String, consumer: TrackInteractor.TrackConsumer) {
		executor.execute {
			consumer.consume(trackRepository.searchTrack(query))
		}
	}

}