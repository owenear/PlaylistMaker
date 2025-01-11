package com.example.playlistmaker.data.settings

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.SharedStorage
import com.example.playlistmaker.domain.settings.api.ThemeRepository


class ThemeRepositoryImpl(private val sharedStorage: SharedStorage) : ThemeRepository {

	override fun getTheme(defaultTheme: Boolean): Boolean {
		return sharedStorage.getData(defaultTheme) as Boolean
	}

	override fun setTheme(nightTheme: Boolean) {
		AppCompatDelegate.setDefaultNightMode(
			if (nightTheme) AppCompatDelegate.MODE_NIGHT_YES
			else AppCompatDelegate.MODE_NIGHT_NO
		)
		saveTheme(nightTheme)
	}


	private fun saveTheme(nightTheme: Boolean) {
		sharedStorage.putData(nightTheme)
	}

}