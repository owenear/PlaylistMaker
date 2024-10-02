package com.example.playlistmaker.domain.settings.api

import com.example.playlistmaker.domain.settings.models.NightTheme

interface ThemeRepository {

	fun saveTheme(nightTheme: NightTheme)

	fun getTheme() : NightTheme

}