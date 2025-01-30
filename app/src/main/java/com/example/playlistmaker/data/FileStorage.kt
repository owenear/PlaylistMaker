package com.example.playlistmaker.data

import android.net.Uri

interface FileStorage {

    fun saveData(saveData: Any): Uri?

}