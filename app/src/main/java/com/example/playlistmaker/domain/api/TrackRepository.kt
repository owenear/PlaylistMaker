package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
	fun searchTrack(query: String): List<Track>?
}