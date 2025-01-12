package com.example.playlistmaker.domain.settings.api


interface ThemeRepository {

	fun getTheme(defaultTheme: Boolean) : Boolean

	fun setTheme(nightTheme : Boolean)


}