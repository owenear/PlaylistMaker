package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.NightThemeDto
import com.example.playlistmaker.domain.api.ThemeRepository
import com.example.playlistmaker.domain.models.NightTheme

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