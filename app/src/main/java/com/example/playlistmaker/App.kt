package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

	private lateinit var sharedPrefs : SharedPreferences

	override fun onCreate() {
		super.onCreate()
		sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)
		val nightTheme = sharedPrefs.getBoolean(NIGHT_THEME_KEY, NIGHT_THEME_DEF)
		switchTheme(nightTheme)
	}

	fun switchTheme(nightThemeEnabled: Boolean) {
		nightTheme = nightThemeEnabled
		AppCompatDelegate.setDefaultNightMode(
			if (nightTheme) {
				AppCompatDelegate.MODE_NIGHT_YES
			} else {
				AppCompatDelegate.MODE_NIGHT_NO
			}
		)
		sharedPrefs.edit()
			.putBoolean(NIGHT_THEME_KEY, nightTheme)
			.apply()
	}

	companion object {
		const val SHARED_PREFERENCES_FILE = "playlist_maker_preferences"
		const val NIGHT_THEME_KEY = "NIGHT_THEME"
		const val NIGHT_THEME_DEF = false
		var nightTheme: Boolean = NIGHT_THEME_DEF
	}

}
