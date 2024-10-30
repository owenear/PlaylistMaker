package com.example.playlistmaker.presentation

import android.app.Application
import android.content.res.Resources.getSystem
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.settings.api.ThemeInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {

	private val themeInteractor : ThemeInteractor by inject()

	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@App)
			modules(dataModule, repositoryModule, interactorModule, viewModelModule)
			androidLogger()
			//logger(PrintLogger(Level.INFO))
		}
		themeInteractor.setTheme(themeInteractor.getTheme())
	}

	companion object {
		val DISPLAY_DENSITY = getSystem().displayMetrics.density
	}
}
