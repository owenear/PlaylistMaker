package com.example.playlistmaker.domain.api

interface ThemeInteractor {
	fun saveTheme(nightTheme : Boolean)
	fun getTheme(): Boolean
}