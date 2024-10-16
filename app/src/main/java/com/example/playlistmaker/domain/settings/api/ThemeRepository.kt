package com.example.playlistmaker.domain.settings.api


interface ThemeRepository {

	fun getTheme() : Boolean

	fun setTheme(nightTheme : Boolean)

}