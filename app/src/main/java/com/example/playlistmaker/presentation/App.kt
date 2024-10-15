package com.example.playlistmaker.presentation

import android.app.Application
import android.content.res.Resources.getSystem
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.settings.api.ThemeInteractor


class App: Application() {

	private lateinit var themeInteractor : ThemeInteractor
	var nightTheme = false

	override fun onCreate() {
		super.onCreate()
		themeInteractor = Creator.provideThemeInteractor(this)
		nightTheme = themeInteractor.getTheme()
		themeInteractor.setTheme(nightTheme)
	}

	/*
	fun switchTheme(newTheme : Boolean) {
		nightTheme = newTheme
		AppCompatDelegate.setDefaultNightMode(
			if (nightTheme) AppCompatDelegate.MODE_NIGHT_YES
			else AppCompatDelegate.MODE_NIGHT_NO
		)
	}
	*/

	companion object {
		val DISPLAY_DENSITY = getSystem().displayMetrics.density
	}
}
