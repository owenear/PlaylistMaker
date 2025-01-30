package com.example.playlistmaker.data.favorites

import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.domain.favorites.api.FavoriteRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.mappers.TrackMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(private val appDatabase: AppDatabase,
                             private val trackMapper: TrackMapper): FavoriteRepository {

    override suspend fun addToFavorites(track: Track) {
        appDatabase.favoriteDao().insertFavorite(trackMapper.map(track))
    }

    override suspend fun removeFromFavorites(track: Track) {
        appDatabase.favoriteDao().deleteFavorite(track.trackId)
    }

    override suspend fun getFavorites(): Flow<List<Track>> {
        return appDatabase.favoriteDao().getFavorites().map {
            trackList -> trackList.map { track -> trackMapper.map(track) }
        }
    }

    override suspend fun getFavoriteByTrackId(trackId: Int): Track? {
        val favorite = appDatabase.favoriteDao().getFavoriteByTrackId(trackId)
        return if (favorite != null) trackMapper.map(favorite) else null
    }

}