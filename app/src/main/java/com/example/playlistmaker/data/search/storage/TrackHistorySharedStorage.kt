package com.example.playlistmaker.data.search.storage

import android.content.SharedPreferences
import com.example.playlistmaker.data.SharedStorage
import com.example.playlistmaker.data.search.dto.TrackHistoryDto
import com.google.gson.Gson

class TrackHistorySharedStorage(private val sharedPreferences: SharedPreferences) : SharedStorage {

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
		const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
		const val SEARCH_HISTORY_DEF = """{"trackList": [] }"""
	}
}