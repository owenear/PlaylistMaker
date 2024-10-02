package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.ThemeRepositoryImpl
import com.example.playlistmaker.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.storage.ThemeSharedStorage
import com.example.playlistmaker.data.storage.TrackHistorySharedStorage
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.ThemeRepository
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.api.TrackHistoryRepository
import com.example.playlistmaker.domain.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

	private fun getTrackRepository(): TrackRepository {
		return TrackRepositoryImpl(RetrofitNetworkClient())
	}

	private fun getHistoryRepository(context: Context): TrackHistoryRepository {
		return TrackHistoryRepositoryImpl(TrackHistorySharedStorage(context))
	}

	private fun getThemeRepository(context: Context): ThemeRepository {
		return ThemeRepositoryImpl(ThemeSharedStorage(context))
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