package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track

interface TrackRepository {
	fun searchTrack(query: String): List<Track>?
}