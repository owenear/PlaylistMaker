package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.TrackHistory

interface TrackHistoryInteractor {
	fun saveHistory(trackHistory: TrackHistory)
	fun getHistory(): TrackHistory
}