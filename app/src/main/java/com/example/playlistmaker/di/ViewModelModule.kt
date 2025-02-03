package com.example.playlistmaker.di

import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker.presentation.playlists.view_model.PlaylistCreateViewModel
import com.example.playlistmaker.presentation.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel { (playlist: Playlist?) ->
        PlaylistCreateViewModel(get(), playlist)
    }

    viewModel { (playlist: Playlist) ->
        PlaylistViewModel(playlist, get(), get())
    }

}