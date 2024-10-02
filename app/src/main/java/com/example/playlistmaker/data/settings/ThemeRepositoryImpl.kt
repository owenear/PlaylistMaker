package com.example.playlistmaker.data.settings

import com.example.playlistmaker.data.SharedStorage
import com.example.playlistmaker.data.settings.dto.NightThemeDto
import com.example.playlistmaker.domain.settings.api.ThemeRepository
import com.example.playlistmaker.domain.settings.models.NightTheme

class ThemeRepositoryImpl(private val sharedStorage: SharedStorage) : ThemeRepository {

	override fun getTheme(): NightTheme {
		val nightThemeDto = sharedStorage.getData() as NightThemeDto
		NightTheme.nightTheme = nightThemeDto.nightTheme
		return NightTheme
	}

	override fun saveTheme(nightTheme: NightTheme) {
		sharedStorage.putData(NightThemeDto(nightTheme.nightTheme))
	}

}