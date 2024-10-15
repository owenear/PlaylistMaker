package com.example.playlistmaker.data.settings

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.SharedStorage
import com.example.playlistmaker.domain.settings.api.ThemeRepository


class ThemeRepositoryImpl(private val sharedStorage: SharedStorage) : ThemeRepository {

	override fun getTheme(): Boolean {
		return sharedStorage.getData() as Boolean
	}

	override fun saveTheme(nightTheme: Boolean) {
		sharedStorage.putData(nightTheme)
	}

	override fun setTheme(nightTheme: Boolean) {
		AppCompatDelegate.setDefaultNightMode(
			if (nightTheme) AppCompatDelegate.MODE_NIGHT_YES
			else AppCompatDelegate.MODE_NIGHT_NO
		)
	}

}