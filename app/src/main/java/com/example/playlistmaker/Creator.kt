package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.ThemeRepositoryImpl
import com.example.playlistmaker.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.SharedRepository
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

	private fun getTrackRepository(): TrackRepository {
		return TrackRepositoryImpl(RetrofitNetworkClient())
	}

	private fun getHistoryRepository(context: Context): SharedRepository {
		return TrackHistoryRepositoryImpl(context)
	}

	private fun getThemeRepository(context: Context): SharedRepository {
		return ThemeRepositoryImpl(context)
	}

	fun provideTracksInteractor(): TracksInteractor {
		return TracksInteractorImpl(getTrackRepository())
	}

	fun provideTrackHistoryInteractor(context: Context): TrackHistoryInteractor {
		return TrackHistoryInteractorImpl(getHistoryRepository(context))
	}

	fun provideThemeInteractor(context: Context): ThemeInteractor {
		return ThemeInteractorImpl(getThemeRepository(context))
	}

}