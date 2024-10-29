package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (previewUrl: String) ->
        PlayerViewModel(previewUrl, get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

}