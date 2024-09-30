package com.example.playlistmaker.domain.models

import androidx.appcompat.app.AppCompatDelegate

object Theme {

	var nightTheme : Boolean = false

	fun switchTheme(newTheme : Boolean) {
		nightTheme = newTheme
		AppCompatDelegate.setDefaultNightMode(
			if (nightTheme) AppCompatDelegate.MODE_NIGHT_YES
			else AppCompatDelegate.MODE_NIGHT_NO
		)
	}

}