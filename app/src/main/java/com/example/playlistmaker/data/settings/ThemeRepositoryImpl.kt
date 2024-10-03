package com.example.playlistmaker.data.settings

import com.example.playlistmaker.data.SharedStorage
import com.example.playlistmaker.domain.settings.api.ThemeRepository


class ThemeRepositoryImpl(private val sharedStorage: SharedStorage) : ThemeRepository {

	override fun getTheme(): Boolean {
		return sharedStorage.getData() as Boolean
	}

	override fun saveTheme(nightTheme: Boolean) {
		sharedStorage.putData(nightTheme)
	}

}