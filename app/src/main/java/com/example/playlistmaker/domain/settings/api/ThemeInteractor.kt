package com.example.playlistmaker.domain.settings.api

import com.example.playlistmaker.domain.settings.models.NightTheme

interface ThemeInteractor {

	fun saveTheme(nightTheme : NightTheme)

	fun getTheme(): NightTheme
}