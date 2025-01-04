package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {

	fun searchTracks(query: String):Flow<Resource<List<Track>>>

}