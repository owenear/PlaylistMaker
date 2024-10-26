package com.example.playlistmaker.domain.settings.api


interface ThemeInteractor {

	fun getTheme(): Boolean

	fun setTheme(nightTheme : Boolean)
}