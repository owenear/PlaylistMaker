package com.example.playlistmaker.data.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.data.SharedStorage
import com.example.playlistmaker.data.dto.NightThemeDto
import com.example.playlistmaker.data.dto.TrackHistoryDto
import com.example.playlistmaker.data.storage.TrackHistorySharedStorage.Companion.SEARCH_HISTORY_DEF
import com.example.playlistmaker.data.storage.TrackHistorySharedStorage.Companion.SEARCH_HISTORY_KEY
import com.google.gson.Gson

class ThemeSharedStorage (context: Context) : SharedStorage {

	private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)

	override fun getData(): Any {
		return Gson().fromJson(
			sharedPreferences.getString(NIGHT_THEME_KEY, NIGHT_THEME_DEF),
			NightThemeDto::class.java)
	}

	override fun putData(data: Any) {
		sharedPreferences.edit()
			.putString(NIGHT_THEME_KEY, Gson().toJson(data))
			.apply()
	}

	companion object {
		const val SHARED_PREFERENCES_FILE = "playlist_maker_preferences"
		const val NIGHT_THEME_KEY = "NIGHT_THEME"
		const val NIGHT_THEME_DEF = """{"nightTheme": "false" }"""
	}

}