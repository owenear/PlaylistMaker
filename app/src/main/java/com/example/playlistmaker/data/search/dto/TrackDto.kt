package com.example.playlistmaker.data.search.dto

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto (
	val trackId: Int,
	val trackName: String?,
	val artistName: String?,
	@SerializedName("trackTimeMillis") val trackTime: Long,
	val artworkUrl100: String?,
	val previewUrl: String?,
	val collectionName: String?,
	val releaseDate: String?,
	val primaryGenreName: String?,
	val country: String?,
	val releaseYear: String?,
	val trackTimeFormat: String,
	) {

		fun getFormatTrackTime(format: String = "ss"): String {
			val minutes = if (trackTime/60000 < 10) "0${(trackTime/60000)}" else (trackTime/60000).toString()
			return minutes + ":" + SimpleDateFormat(format,Locale.getDefault()).format(trackTime)
		}

		fun getYearRelease(): String {
			return if (!releaseDate.isNullOrEmpty())
				releaseDate.substring(0..3)
			else ""
		}

}