package com.example.playlistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects

@Parcelize
data class Track (
	val trackId: Int?,
	val trackName: String?,
	val artistName: String?,
	@SerializedName("trackTimeMillis") val trackTime: Int?,
	val artworkUrl100: String?,
	val previewUrl: String?,
	val collectionName: String?,
	val releaseDate: String?,
	val primaryGenreName: String?,
	val country: String?
): Parcelable {


	override fun hashCode(): Int {
		return Objects.hash(trackId)
	}

	override fun equals(other: Any?): Boolean {
		if (other == null || other !is Track)
			return false
		return trackId == other.trackId
	}

	fun getFormatTrackTime(format: String): String {
		return SimpleDateFormat(format,
			Locale.getDefault()).format(trackTime)
	}

	fun getReleaseYear(): String {
		return if (!releaseDate.isNullOrEmpty())
			releaseDate.substring(0..3)
		else ""
	}

}

class SearchResponse(@SerializedName("results") val searchList: List<Track>)

interface ItunesApi {
	@GET("/search")
	fun search(@Query("term") text: String) : Call<SearchResponse>
}

