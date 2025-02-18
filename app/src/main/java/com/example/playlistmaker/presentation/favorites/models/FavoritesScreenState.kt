package com.example.playlistmaker.presentation.favorites.models

import com.example.playlistmaker.domain.search.models.Track


interface FavoritesScreenState {
    data object Empty : FavoritesScreenState
    data class Content(val trackList: List<Track>) : FavoritesScreenState
}