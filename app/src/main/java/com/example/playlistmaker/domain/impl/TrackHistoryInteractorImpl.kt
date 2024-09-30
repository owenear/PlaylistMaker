package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SharedRepository
import com.example.playlistmaker.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.models.TrackHistory

class TrackHistoryInteractorImpl(private val sharedRepository: SharedRepository) : TrackHistoryInteractor {

	override fun saveHistory(trackHistory: TrackHistory) {
		sharedRepository.putData(trackHistory.trackList)
	}

	override fun getHistory(): TrackHistory {
		return sharedRepository.getData() as TrackHistory
	}

}