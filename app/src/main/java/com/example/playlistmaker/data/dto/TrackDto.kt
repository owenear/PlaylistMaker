package com.example.playlistmaker.data.dto

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto (
	val trackId: Int,
	val trackName: String?,
	val artistName: String?,
	@SerializedName("trackTimeMillis") val trackTime: Int,
	val artworkUrl100: String?,
	val previewUrl: String?,
	val collectionName: String?,
	val releaseDate: String?,
	val primaryGenreName: String?,
	val country: String?
	) {

		fun getFormatTrackTime(format: String): String {
			Log.d("TIME TO FORMAT", trackTime.toString())
			Log.d("FORMAT", SimpleDateFormat(format,Locale.getDefault()).format(trackTime))
			return SimpleDateFormat(format,
				Locale.getDefault()).format(trackTime)
		}

		fun getReleaseYear(): String {
			return if (!releaseDate.isNullOrEmpty())
				releaseDate.substring(0..3)
			else ""
		}

}