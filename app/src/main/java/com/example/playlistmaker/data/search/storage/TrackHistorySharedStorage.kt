package com.example.playlistmaker.data.search.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.data.SharedStorage
import com.example.playlistmaker.data.search.dto.TrackHistoryDto
import com.google.gson.Gson

class TrackHistorySharedStorage(context: Context) : SharedStorage {

	private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)

	override fun getData(): Any {
		return Gson().fromJson(
				sharedPreferences.getString(SEARCH_HISTORY_KEY, SEARCH_HISTORY_DEF),
				TrackHistoryDto::class.java)
	}

	override fun putData(data: Any) {
		sharedPreferences.edit()
			.putString(SEARCH_HISTORY_KEY, Gson().toJson(data))
			.apply()
	}

	companion object {
		const val SHARED_PREFERENCES_FILE = "playlist_maker_preferences"
		const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
		const val SEARCH_HISTORY_DEF = """{"trackList": [] }"""
	}
}