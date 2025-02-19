package com.example.playlistmaker.data.favorites.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.favorites.dto.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE itunesId = :itunesId")
    suspend fun deleteFavorite(itunesId: Int)

    @Query("SELECT * FROM favorites ORDER BY trackId DESC")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE itunesId = :itunesId")
    suspend fun getFavoriteByTrackId(itunesId: Int): FavoriteEntity?
}