package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.ThemeRepository
import com.example.playlistmaker.domain.settings.api.ThemeInteractor

class ThemeInteractorImpl(private val themeRepository: ThemeRepository) : ThemeInteractor {

	override fun saveTheme(nightTheme: Boolean) {
		themeRepository.saveTheme(nightTheme)
	}

	override fun getTheme() : Boolean {
		return themeRepository.getTheme()
	}

	override fun setTheme(nightTheme: Boolean) {
		themeRepository.setTheme(nightTheme)
	}

}