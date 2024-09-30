package com.example.playlistmaker.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.domain.api.SharedRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TrackHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackHistoryRepositoryImpl(private val context: Context) : SharedRepository {

	private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)

	override fun getData(): Any {
		val listType = object : TypeToken<List<Track>>() {}.type
		return TrackHistory(
					Gson().fromJson(sharedPreferences.getString(SEARCH_HISTORY_KEY, SEARCH_HISTORY_DEF),
					listType
				)
		)
	}

	override fun putData(data: Any) {
		sharedPreferences.edit()
			.putString(SEARCH_HISTORY_KEY, Gson().toJson(data))
			.apply()
	}

	companion object {
		const val SHARED_PREFERENCES_FILE = "playlist_maker_preferences"
		const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
		const val SEARCH_HISTORY_DEF = "[]"
	}

}