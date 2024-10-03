package com.example.playlistmaker.data.settings.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.data.SharedStorage

class ThemeSharedStorage (context: Context) : SharedStorage {

	private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)

	override fun getData(): Any {
		return sharedPreferences.getBoolean(NIGHT_THEME_KEY, NIGHT_THEME_DEF)
	}

	override fun putData(data: Any) {
		sharedPreferences.edit()
			.putBoolean(NIGHT_THEME_KEY, data as Boolean)
			.apply()
	}

	companion object {
		const val SHARED_PREFERENCES_FILE = "playlist_maker_preferences"
		const val NIGHT_THEME_KEY = "NIGHT_THEME"
		const val NIGHT_THEME_DEF = false
	}

}