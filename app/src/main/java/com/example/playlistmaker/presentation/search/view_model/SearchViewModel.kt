package com.example.playlistmaker.presentation.search.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.api.TrackInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.search.models.SearchScreenState
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.util.Resource

class SearchViewModel(
	private val trackInteractor: TrackInteractor,
	private val trackHistoryInteractor: TrackHistoryInteractor) : ViewModel() {

	private val handler = Handler(Looper.getMainLooper())

	private val stateMutableLiveData = MutableLiveData<SearchScreenState>(SearchScreenState.Initial)
	val stateLiveData : LiveData<SearchScreenState> = stateMutableLiveData

	private val queryMutableLiveData = MutableLiveData<String>()
	val queryLiveData : LiveData<String> = queryMutableLiveData

	private val trackHistory = trackHistoryInteractor.getHistory()


	fun processTheQuery(query: String){
		when {
			query.isEmpty() -> {
				handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
				queryMutableLiveData.postValue(query)
				renderState(SearchScreenState.HistoryContent(trackHistory.trackList))
			}
			query != queryMutableLiveData.value.toString() -> {
				renderState(SearchScreenState.Initial)
				searchDebounce(query)
			}
		}
	}

	fun addToHistory(track: Track){
		trackHistory.addTrack(track)
		trackHistoryInteractor.saveHistory(trackHistory)
	}

	fun clearHistory(){
		trackHistory.clear()
		trackHistoryInteractor.saveHistory(trackHistory)
		renderState(SearchScreenState.HistoryContent(trackHistory.trackList))
	}

	fun search(query: String) {
		queryMutableLiveData.postValue(query)
		handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
		if (query.isNotEmpty()) {
			renderState(SearchScreenState.Loading)
			trackInteractor.searchTracks(query,
				object : TrackInteractor.TrackConsumer {
					override fun consume(found: Resource<List<Track>>) {
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
				}
			)
		}
	}

	private fun searchDebounce(query: String) {
		val searchRunnable = Runnable { search(query) }
		handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
		handler.postAtTime(searchRunnable,SEARCH_REQUEST_TOKEN,
			SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY)
	}

	fun renderState(state: SearchScreenState) {
		stateMutableLiveData.postValue(state)
	}

	override fun onCleared() {
		trackHistoryInteractor.saveHistory(trackHistory)
		handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
		super.onCleared()
	}

	companion object {
		fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
			initializer {
				SearchViewModel(
					Creator.provideTrackInteractor(context),
					Creator.provideTrackHistoryInteractor(context))
			}
		}
		private const val SEARCH_DEBOUNCE_DELAY = 2000L
		private val SEARCH_REQUEST_TOKEN = Any()
	}

}