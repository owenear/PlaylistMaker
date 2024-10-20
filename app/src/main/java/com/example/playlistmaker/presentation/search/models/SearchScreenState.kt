package com.example.playlistmaker.presentation.search.models

import com.example.playlistmaker.domain.search.models.Track

sealed interface SearchScreenState {

	data object Initial : SearchScreenState

	data object Loading : SearchScreenState

	data object Error : SearchScreenState

	data object Empty : SearchScreenState

	data class SearchContent(
		val trackList: List<Track>
	) : SearchScreenState

	data class HistoryContent(
		val trackList: List<Track>
	) :SearchScreenState

}