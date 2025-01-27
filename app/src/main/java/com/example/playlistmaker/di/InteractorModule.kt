package com.example.playlistmaker.di

import com.example.playlistmaker.domain.favorites.api.FavoriteInteractor
import com.example.playlistmaker.domain.favorites.impl.FavoriteInteractorImpl
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.impl.MediaPlayerInteractorImpl
import com.example.playlistmaker.domain.playlists.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.impl.PlaylistInteractorImpl
import com.example.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.api.TrackInteractor
import com.example.playlistmaker.domain.search.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.settings.api.ThemeInteractor
import com.example.playlistmaker.domain.settings.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<MediaPlayerInteractor>{
        MediaPlayerInteractorImpl(get())
    }

    single<TrackInteractor>{
        TrackInteractorImpl(get())
    }

    single<TrackHistoryInteractor>{
        TrackHistoryInteractorImpl(get())
    }

    single<ThemeInteractor>{
        ThemeInteractorImpl(get())
    }

    single<SharingInteractor>{
        SharingInteractorImpl(get())
    }

    single<FavoriteInteractor>{
        FavoriteInteractorImpl(get())
    }

    single<PlaylistInteractor>{
        PlaylistInteractorImpl(get())
    }

}