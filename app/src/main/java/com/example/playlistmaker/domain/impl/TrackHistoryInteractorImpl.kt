package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.api.TrackHistoryRepository
import com.example.playlistmaker.domain.models.TrackHistory

class TrackHistoryInteractorImpl(private val trackHistoryRepository: TrackHistoryRepository) : TrackHistoryInteractor {

	override fun saveHistory(trackHistory: TrackHistory) {
		trackHistoryRepository.saveHistory(trackHistory)
	}

	override fun getHistory(): TrackHistory {
		return trackHistoryRepository.getHistory()
	}

}