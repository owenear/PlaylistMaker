package com.example.playlistmaker

import android.app.Application
import android.content.res.Resources.getSystem
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.ThemeInteractor

class App: Application() {

	private lateinit var themeInteractor : ThemeInteractor
	var nightTheme: Boolean = false

	override fun onCreate() {
		super.onCreate()
		themeInteractor = Creator.provideThemeInteractor(this)
		nightTheme = themeInteractor.getTheme()
		switchTheme(nightTheme)
	}

	fun switchTheme(nightThemeEnabled: Boolean) {
		nightTheme = nightThemeEnabled
		AppCompatDelegate.setDefaultNightMode(
			if (nightTheme) AppCompatDelegate.MODE_NIGHT_YES
			else AppCompatDelegate.MODE_NIGHT_NO
		)
	}

	fun saveTheme(){
		themeInteractor.saveTheme(nightTheme)
	}

	companion object {
		var DISPLAY_DENSITY = getSystem().displayMetrics.density
	}

}
