package com.example.playlistmaker.presentation.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.api.TrackInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.search.models.SearchScreenState
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
	private val trackInteractor: TrackInteractor,
	private val trackHistoryInteractor: TrackHistoryInteractor) : ViewModel() {

	private val stateMutableLiveData = MutableLiveData<SearchScreenState>(SearchScreenState.Initial)
	val stateLiveData : LiveData<SearchScreenState> = stateMutableLiveData

	private var query: String = ""
	private val trackHistory = trackHistoryInteractor.getHistory()

	private var searchJob: Job? = null

	fun processQuery(query: String){
		when {
			query.isEmpty() -> {
				searchJob?.cancel()
				this.query = query
				if (trackHistory.trackList.isNotEmpty())
					renderState(SearchScreenState.HistoryContent(trackHistory.trackList))
			}
			query != this.query  -> {
				renderState(SearchScreenState.Initial)
				searchDebounce(query)
			}
		}
	}

	fun processInitial() {
		renderState(SearchScreenState.Initial)
	}

	fun addToHistory(track: Track){
		trackHistory.addTrack(track)
		trackHistoryInteractor.saveHistory(trackHistory)
	}

	fun clearHistory(){
		trackHistory.clear()
		trackHistoryInteractor.saveHistory(trackHistory)
		renderState(SearchScreenState.Initial)
	}

	private fun searchDebounce(query: String) {
		searchJob?.cancel()
		searchJob = viewModelScope.launch {
			delay(SEARCH_DEBOUNCE_DELAY)
			search(query)
		}
	}

	fun search(query: String) {
		this.query = query
		searchJob?.cancel()
		if (query.isNotEmpty()) {
			renderState(SearchScreenState.Loading)
			viewModelScope.launch {
				trackInteractor
					.searchTracks(query)
					.collect { found -> processResult(found) }
			}
		}
	}

	private fun processResult(found: Resource<List<Track>>) {
		when (found) {
			is Resource.Error -> {
				if (found.code == 404)
					renderState(SearchScreenState.Empty)
				else
					renderState(SearchScreenState.Error)
			}
			is Resource.Success -> {
				renderState(SearchScreenState.SearchContent(
					found.data ?: listOf()))
			}
		}
	}

	private fun renderState(state: SearchScreenState) {
		stateMutableLiveData.postValue(state)
	}

	override fun onCleared() {
		trackHistoryInteractor.saveHistory(trackHistory)
		super.onCleared()
	}

	companion object {
		private const val SEARCH_DEBOUNCE_DELAY = 2000L
	}

}