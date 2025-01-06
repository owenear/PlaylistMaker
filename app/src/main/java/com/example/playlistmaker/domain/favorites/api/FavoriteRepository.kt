package com.example.playlistmaker.domain.favorites.api

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addToFavorites(track: Track)

    suspend fun removeFromFavorites(track: Track)

    fun getFavorites() : Flow<List<Track>>

    suspend fun getFavoriteByTrackId(trackId: Int) : Track?
}