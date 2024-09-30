package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val networkRepository: TrackRepository) : TrackInteractor {

	private val executor = Executors.newCachedThreadPool()

	override fun searchTracks(query: String, consumer: TrackInteractor.TrackConsumer) {
		executor.execute {
			consumer.consume(networkRepository.searchTrack(query))
		}
	}

}