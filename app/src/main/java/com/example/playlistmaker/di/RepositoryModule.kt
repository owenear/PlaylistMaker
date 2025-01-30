package com.example.playlistmaker.di

import com.example.playlistmaker.data.favorites.FavoriteRepositoryImpl
import com.example.playlistmaker.data.player.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.playlists.PlaylistRepositoryImpl
import com.example.playlistmaker.data.search.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.search.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.ThemeRepositoryImpl
import com.example.playlistmaker.data.sharing.SharingRepositoryImpl
import com.example.playlistmaker.domain.favorites.api.FavoriteRepository
import com.example.playlistmaker.domain.player.api.MediaPlayerRepository
import com.example.playlistmaker.domain.playlists.api.PlaylistRepository
import com.example.playlistmaker.domain.search.api.TrackHistoryRepository
import com.example.playlistmaker.domain.search.api.TrackRepository
import com.example.playlistmaker.domain.settings.api.ThemeRepository
import com.example.playlistmaker.domain.sharing.api.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory<MediaPlayerRepository>{
        MediaPlayerRepositoryImpl(get())
    }

    single<TrackRepository>{
        TrackRepositoryImpl(get(), get())
    }

    single<TrackHistoryRepository>{
        TrackHistoryRepositoryImpl(get(qualifier = named("track_history")), get())
    }

    single<ThemeRepository>{
        ThemeRepositoryImpl(get(qualifier = named("theme")))
    }

    single<SharingRepository>{
        SharingRepositoryImpl(androidContext())
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get())
    }

}