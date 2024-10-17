package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.Resource

interface TrackRepository {
	fun searchTrack(query: String): Resource<List<Track>>
}