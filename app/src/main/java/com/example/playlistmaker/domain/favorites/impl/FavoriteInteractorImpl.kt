package com.example.playlistmaker.domain.favorites.impl

import com.example.playlistmaker.domain.favorites.api.FavoriteInteractor
import com.example.playlistmaker.domain.favorites.api.FavoriteRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository)
    : FavoriteInteractor {

    override suspend fun addToFavorites(track: Track) {
        favoriteRepository.addToFavorites(track)
    }

    override suspend fun removeFromFavorites(track: Track) {
        favoriteRepository.removeFromFavorites(track)
    }

    override fun getFavorites(): Flow<List<Track>> {
        return favoriteRepository.getFavorites()
    }

    override suspend fun isFavorite(track: Track): Boolean  =
        favoriteRepository.getFavoriteByTrackId(track.trackId) != null
}