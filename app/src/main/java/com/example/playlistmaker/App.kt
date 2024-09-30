package com.example.playlistmaker

import android.app.Application
import android.content.res.Resources.getSystem
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.models.Theme

class App: Application() {

	private lateinit var themeInteractor : ThemeInteractor

	override fun onCreate() {
		super.onCreate()
		themeInteractor = Creator.provideThemeInteractor(this)
		Theme.switchTheme(themeInteractor.getTheme())
	}

	companion object {
		var DISPLAY_DENSITY = getSystem().displayMetrics.density
	}

}
