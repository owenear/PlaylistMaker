package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.ThemeRepository
import com.example.playlistmaker.domain.settings.api.ThemeInteractor

class ThemeInteractorImpl(private val themeRepository: ThemeRepository) : ThemeInteractor {

	override fun getTheme(defaultTheme: Boolean) : Boolean {
		return themeRepository.getTheme(defaultTheme)
	}

	override fun setTheme(nightTheme: Boolean) {
		themeRepository.setTheme(nightTheme)
	}


}