package com.example.playlistmaker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.favorites.db.FavoriteDao
import com.example.playlistmaker.data.favorites.dto.FavoriteEntity
import com.example.playlistmaker.data.playlists.db.PlaylistDao
import com.example.playlistmaker.data.playlists.dto.PlaylistEntity
import com.example.playlistmaker.data.playlists.dto.PlaylistTrackCrossRefEntity
import com.example.playlistmaker.data.playlists.dto.TrackEntity

@Database(version = 1, entities = [FavoriteEntity::class, PlaylistEntity::class,
    TrackEntity::class, PlaylistTrackCrossRefEntity::class]
    )

abstract class AppDatabase : RoomDatabase(){

    abstract fun favoriteDao(): FavoriteDao

    abstract fun playlistDao(): PlaylistDao

}