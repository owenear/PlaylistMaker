package com.example.playlistmaker.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.data.player.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.settings.ThemeRepositoryImpl
import com.example.playlistmaker.data.search.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.search.TrackRepositoryImpl
import com.example.playlistmaker.data.search.dto.TrackHistoryMapper
import com.example.playlistmaker.data.search.dto.TrackMapper
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.storage.ThemeSharedStorage
import com.example.playlistmaker.data.search.storage.TrackHistorySharedStorage
import com.example.playlistmaker.data.sharing.SharingRepositoryImpl
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
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.api.SharingRepository
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {

	private const val SHARED_PREFERENCES_FILE = "playlist_maker_preferences"

	private fun getTrackMapper(): TrackMapper{
		return TrackMapper()
	}

	private fun getTrackHistoryMapper(): TrackHistoryMapper {
		return TrackHistoryMapper()
	}

	private fun getSharedPreferences(context: Context): SharedPreferences {
		return context.getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)
	}

	private fun getMediaPlayer(): MediaPlayer {
		return MediaPlayer()
	}

	private fun getRetrofitNetworkClient(context: Context): RetrofitNetworkClient {
		return RetrofitNetworkClient(context)
	}

	private fun getHistorySharedStorage(context: Context): TrackHistorySharedStorage {
		return TrackHistorySharedStorage(getSharedPreferences(context))
	}

	private fun getThemeSharedStorage(context: Context): ThemeSharedStorage {
		return ThemeSharedStorage(getSharedPreferences(context))
	}

	private fun getTrackRepository(context: Context): TrackRepository {
		return TrackRepositoryImpl(getRetrofitNetworkClient(context), getTrackMapper())
	}

	private fun getHistoryRepository(context: Context): TrackHistoryRepository {
		return TrackHistoryRepositoryImpl(getHistorySharedStorage(context), getTrackHistoryMapper())
	}

	private fun getThemeRepository(context: Context): ThemeRepository {
		return ThemeRepositoryImpl(getThemeSharedStorage(context))
	}

	private fun getMediaPlayerRepository(): MediaPlayerRepository {
		return MediaPlayerRepositoryImpl(getMediaPlayer())
	}

	private fun getSharingRepository(context: Context): SharingRepository {
		return SharingRepositoryImpl(context)
	}

	fun provideTrackInteractor(context: Context): TrackInteractor {
		return TrackInteractorImpl(getTrackRepository(context))
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

	fun provideSharingInteractor(context: Context): SharingInteractor {
		return SharingInteractorImpl(getSharingRepository(context))
	}

}