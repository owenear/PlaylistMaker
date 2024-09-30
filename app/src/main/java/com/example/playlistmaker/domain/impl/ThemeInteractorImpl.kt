package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SharedRepository
import com.example.playlistmaker.domain.api.ThemeInteractor

class ThemeInteractorImpl(private val sharedRepository: SharedRepository) : ThemeInteractor{

	override fun saveTheme(nightTheme: Boolean) {
		sharedRepository.putData(nightTheme)
	}

	override fun getTheme() : Boolean {
		return sharedRepository.getData() as Boolean
	}

}