package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.ThemeRepository
import com.example.playlistmaker.domain.settings.api.ThemeInteractor
import com.example.playlistmaker.domain.settings.models.NightTheme

class ThemeInteractorImpl(private val themeRepository: ThemeRepository) : ThemeInteractor {

	override fun saveTheme(nightTheme: NightTheme) {
		themeRepository.saveTheme(nightTheme)
	}

	override fun getTheme() : NightTheme {
		return themeRepository.getTheme()
	}
}