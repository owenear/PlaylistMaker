package com.example.playlistmaker.domain.settings.api


interface ThemeInteractor {

	fun getTheme(defaultTheme: Boolean): Boolean

	fun setTheme(nightTheme : Boolean)
}