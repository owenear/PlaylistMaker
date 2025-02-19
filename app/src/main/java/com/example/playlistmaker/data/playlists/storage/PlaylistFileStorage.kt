package com.example.playlistmaker.data.playlists.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.data.FileStorage
import com.example.playlistmaker.data.playlists.dto.PlaylistEntity
import java.io.File
import java.io.FileOutputStream

class PlaylistFileStorage(private val context: Context) : FileStorage {

    override fun saveData(saveData: Any): Uri? {
        val playlist = saveData as PlaylistEntity
        if (playlist.coverUri.isNullOrEmpty()) return null
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PLAYLIST_COVER_DIR)
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, "${playlist.playlistId}.jpg")
        if (file.toUri() == playlist.coverUri.toUri()) return playlist.coverUri.toUri()
        val inputStream = context.contentResolver.openInputStream(playlist.coverUri.toUri())
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri()
    }

    companion object {
        const val PLAYLIST_COVER_DIR = "playlist_covers"
    }
}