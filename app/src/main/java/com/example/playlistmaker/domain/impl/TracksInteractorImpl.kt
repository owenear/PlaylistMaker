package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val networkRepository: TrackRepository) : TracksInteractor {

	private val executor = Executors.newCachedThreadPool()

	override fun searchTracks(query: String, consumer: TracksInteractor.TrackConsumer) {
		executor.execute {
			consumer.consume(networkRepository.searchTrack(query))
		}
	}

}