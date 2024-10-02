package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.api.TrackHistoryRepository
import com.example.playlistmaker.domain.search.models.TrackHistory

class TrackHistoryInteractorImpl(private val trackHistoryRepository: TrackHistoryRepository) :
	TrackHistoryInteractor {

	override fun saveHistory(trackHistory: TrackHistory) {
		trackHistoryRepository.saveHistory(trackHistory)
	}

	override fun getHistory(): TrackHistory {
		return trackHistoryRepository.getHistory()
	}

}