package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.player.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.settings.ThemeRepositoryImpl
import com.example.playlistmaker.data.search.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.search.TrackRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.storage.ThemeSharedStorage
import com.example.playlistmaker.data.search.storage.TrackHistorySharedStorage
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.api.MediaPlayerRepository
import com.example.playlistmaker.domain.search.api.TrackInteractor
import com.example.playlistmaker.domain.search.api.TrackRepository
import com.example.playlistmaker.domain.settings.api.ThemeRepository
import com.example.playlistmaker.domain.settings.api.ThemeInteractor
import com.example.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.api.TrackHistoryRepository
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.settings.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl

object Creator {

	private fun getRetrofitNetworkClient(): RetrofitNetworkClient {
		return RetrofitNetworkClient()
	}

	private fun getHistorySharedStorage(context: Context): TrackHistorySharedStorage {
		return TrackHistorySharedStorage(context)
	}

	private fun getThemeSharedStorage(context: Context): ThemeSharedStorage {
		return ThemeSharedStorage(context)
	}

	private fun getTrackRepository(): TrackRepository {
		return TrackRepositoryImpl(getRetrofitNetworkClient())
	}

	private fun getHistoryRepository(context: Context): TrackHistoryRepository {
		return TrackHistoryRepositoryImpl(getHistorySharedStorage(context))
	}

	private fun getThemeRepository(context: Context): ThemeRepository {
		return ThemeRepositoryImpl(getThemeSharedStorage(context))
	}

	private fun getMediaPlayerRepository(): MediaPlayerRepository {
		return MediaPlayerRepositoryImpl()
	}

	fun provideTracksInteractor(): TrackInteractor {
		return TrackInteractorImpl(getTrackRepository())
	}

	fun provideTrackHistoryInteractor(context: Context): TrackHistoryInteractor {
		return TrackHistoryInteractorImpl(getHistoryRepository(context))
	}

	fun provideThemeInteractor(context: Context): ThemeInteractor {
		return ThemeInteractorImpl(getThemeRepository(context))
	}

	fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
		return MediaPlayerInteractorImpl(getMediaPlayerRepository())
	}

}