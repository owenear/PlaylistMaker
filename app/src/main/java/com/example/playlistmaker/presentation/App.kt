package com.example.playlistmaker.presentation

import android.app.Application
import android.content.res.Resources.getSystem
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.settings.api.ThemeInteractor


class App: Application() {

	private lateinit var themeInteractor : ThemeInteractor

	override fun onCreate() {
		super.onCreate()
		themeInteractor = Creator.provideThemeInteractor(this)
		themeInteractor.setTheme(themeInteractor.getTheme())
	}

	companion object {
		val DISPLAY_DENSITY = getSystem().displayMetrics.density
	}
}
