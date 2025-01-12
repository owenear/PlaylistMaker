package com.example.playlistmaker.data.favorites.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.favorites.dto.FavoriteEntity

@Database(version = 1, entities = [FavoriteEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun favoriteDao(): FavoriteDao

}