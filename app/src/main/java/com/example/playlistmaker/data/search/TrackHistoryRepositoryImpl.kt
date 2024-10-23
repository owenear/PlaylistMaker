package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.SharedStorage
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.data.search.dto.TrackHistoryDto
import com.example.playlistmaker.data.search.dto.TrackHistoryMapper
import com.example.playlistmaker.data.search.dto.TrackMapper
import com.example.playlistmaker.domain.search.api.TrackHistoryRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TrackHistory

class TrackHistoryRepositoryImpl(private val sharedStorage: SharedStorage,
								 private val trackHistoryMapper: TrackHistoryMapper)
	: TrackHistoryRepository {

	override fun getHistory(): TrackHistory {
		val trackHistoryDto = sharedStorage.getData() as TrackHistoryDto
		return trackHistoryMapper.map(trackHistoryDto)
	}

	override fun saveHistory(trackHistory: TrackHistory) {
		sharedStorage.putData(trackHistoryMapper.map(trackHistory))
	}

}