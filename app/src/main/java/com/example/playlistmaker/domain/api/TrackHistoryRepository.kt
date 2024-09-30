package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackHistory

interface TrackHistoryRepository {

	fun saveHistory(trackHistory: TrackHistory)

	fun getHistory() : TrackHistory

}