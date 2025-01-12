package com.example.playlistmaker.domain.favorites.api

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    suspend fun addToFavorites(track: Track)

    suspend fun removeFromFavorites(track: Track)

    suspend fun getFavorites() : Flow<List<Track>>

    suspend fun isFavorite(track: Track) : Boolean
}