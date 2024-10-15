package com.example.playlistmaker.domain.settings.api


interface ThemeInteractor {

	fun saveTheme(nightTheme : Boolean)

	fun getTheme(): Boolean

	fun setTheme(nightTheme : Boolean)
}