package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.ThemeRepository
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.models.NightTheme

class ThemeInteractorImpl(private val themeRepository: ThemeRepository) : ThemeInteractor{

	override fun saveTheme(nightTheme: NightTheme) {
		themeRepository.saveTheme(nightTheme)
	}

	override fun getTheme() : NightTheme {
		return themeRepository.getTheme()
	}
}