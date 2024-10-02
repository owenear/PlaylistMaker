package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.NightTheme

interface ThemeInteractor {

	fun saveTheme(nightTheme : NightTheme)

	fun getTheme(): NightTheme
}