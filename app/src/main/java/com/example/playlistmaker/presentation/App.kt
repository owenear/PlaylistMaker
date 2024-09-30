package com.example.playlistmaker.presentation

import android.app.Application
import android.content.res.Resources.getSystem
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.models.NightTheme


class App: Application() {

	private lateinit var themeInteractor : ThemeInteractor

	override fun onCreate() {
		super.onCreate()
		themeInteractor = Creator.provideThemeInteractor(this)
		switchTheme(themeInteractor.getTheme().nightTheme)
	}

	fun switchTheme(nightTheme : Boolean) {
		NightTheme.nightTheme = nightTheme
		AppCompatDelegate.setDefaultNightMode(
			if (nightTheme) AppCompatDelegate.MODE_NIGHT_YES
			else AppCompatDelegate.MODE_NIGHT_NO
		)
	}

	companion object {
		var DISPLAY_DENSITY = getSystem().displayMetrics.density
	}

}
