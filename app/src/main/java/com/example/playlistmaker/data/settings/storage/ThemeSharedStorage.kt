package com.example.playlistmaker.data.settings.storage

import android.content.SharedPreferences
import com.example.playlistmaker.data.SharedStorage

class ThemeSharedStorage (private val sharedPreferences: SharedPreferences) : SharedStorage {

	override fun getData(): Any {
		return sharedPreferences.getBoolean(NIGHT_THEME_KEY, NIGHT_THEME_DEF)
	}

	override fun putData(data: Any) {
		sharedPreferences.edit()
			.putBoolean(NIGHT_THEME_KEY, data as Boolean)
			.apply()
	}

	companion object {
		const val NIGHT_THEME_KEY = "NIGHT_THEME"
		const val NIGHT_THEME_DEF = false
	}

}