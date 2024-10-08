package com.example.playlistmaker.data.search.dto

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
	val country: String?,
	val releaseYear: String?,
	val trackTimeFormat: String,
	) {

		fun getFormatTrackTime(format: String): String {
			return SimpleDateFormat(format,
				Locale.getDefault()).format(trackTime)
		}

		fun getYearRelease(): String {
			return if (!releaseDate.isNullOrEmpty())
				releaseDate.substring(0..3)
			else ""
		}

}