package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

	lateinit var sharedPreferences : SharedPreferences
	var nightTheme: Boolean = NIGHT_THEME_DEF

	override fun onCreate() {
		super.onCreate()
		sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)
		nightTheme = sharedPreferences.getBoolean(NIGHT_THEME_KEY, nightTheme)
		switchTheme(nightTheme)
	}

	fun switchTheme(nightThemeEnabled: Boolean) {
		nightTheme = nightThemeEnabled
		AppCompatDelegate.setDefaultNightMode(
			if (nightTheme) AppCompatDelegate.MODE_NIGHT_YES
			else AppCompatDelegate.MODE_NIGHT_NO
		)
	}

	fun saveTheme(){
		sharedPreferences.edit()
			.putBoolean(NIGHT_THEME_KEY, nightTheme)
			.apply()
	}

	companion object {
		const val SHARED_PREFERENCES_FILE = "playlist_maker_preferences"
		const val NIGHT_THEME_KEY = "NIGHT_THEME"
		const val NIGHT_THEME_DEF = false
	}

}
