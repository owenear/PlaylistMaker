package com.example.playlistmaker.data.favorites.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE track_id = :trackId")
    suspend fun deleteFavorite(trackId: Int)

    @Query("SELECT * FROM favorites ORDER BY id DESC")
    suspend fun getFavorites(): List<FavoriteEntity>

    @Query("SELECT * FROM favorites WHERE track_id = :trackId")
    suspend fun getFavoriteByTrackId(trackId: Int): FavoriteEntity?
}