package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.SharedStorage
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.data.search.dto.TrackHistoryDto
import com.example.playlistmaker.domain.search.api.TrackHistoryRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TrackHistory

class TrackHistoryRepositoryImpl(private val sharedStorage: SharedStorage) :
	TrackHistoryRepository {

	override fun getHistory(): TrackHistory {
		val trackHistoryDto = sharedStorage.getData() as TrackHistoryDto
		val trackHistoryList = trackHistoryDto.trackList.map {
			Track (
				it.trackId,
				it.trackName,
				it.artistName,
				it.trackTimeFormat,
				it.artworkUrl100,
				it.previewUrl,
				it.collectionName,
				it.releaseYear,
				it.primaryGenreName,
				it.country
			)
		}
		return TrackHistory(trackHistoryList.toMutableList())
	}

	override fun saveHistory(trackHistory: TrackHistory) {
		val trackHistoryDto = TrackHistoryDto(trackHistory.trackList.map {
			TrackDto (
					it.trackId,
					it.trackName,
					it.artistName,
					0,
					it.artworkUrl100,
					it.previewUrl,
					it.collectionName,
					it.releaseYear,
					it.primaryGenreName,
					it.country,
					it.releaseYear,
					it.trackTimeFormat
			)
		})
		sharedStorage.putData(trackHistoryDto)
	}

}