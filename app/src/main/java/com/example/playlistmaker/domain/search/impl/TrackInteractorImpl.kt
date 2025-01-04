package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.TrackInteractor
import com.example.playlistmaker.domain.search.api.TrackRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

class TrackInteractorImpl(private val trackRepository: TrackRepository) : TrackInteractor {

	override fun searchTracks(query: String) : Flow<Resource<List<Track>>> {
		return trackRepository.searchTrack(query)
	}

}