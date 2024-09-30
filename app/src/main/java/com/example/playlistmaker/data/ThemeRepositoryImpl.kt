package com.example.playlistmaker.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.domain.api.SharedRepository

class ThemeRepositoryImpl(private val context: Context) : SharedRepository {

	private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)

	override fun getData(): Any {
		return sharedPreferences.getBoolean(NIGHT_THEME_KEY, false)
	}

	override fun putData(data: Any) {
		sharedPreferences.edit()
			.putBoolean(NIGHT_THEME_KEY, data as Boolean)
			.apply()
	}

	companion object {
		const val SHARED_PREFERENCES_FILE = "playlist_maker_preferences"
		const val NIGHT_THEME_KEY = "NIGHT_THEME"
	}

}