package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.library.view_model.FavoritesViewModel
import com.example.playlistmaker.presentation.library.view_model.PlaylistCreateViewModel
import com.example.playlistmaker.presentation.library.view_model.PlaylistsViewModel
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get())
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
        PlaylistsViewModel()
    }

    viewModel {
        PlaylistCreateViewModel()
    }

}