package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Objects

data class Track (
	val trackId: Int,
	val trackName: String,
	val artistName: String,
	@SerializedName("trackTimeMillis") val trackTime: Int,
	val artworkUrl100: String
) {

	override fun hashCode(): Int {
		return Objects.hash(trackId)
	}

	override fun equals(other: Any?): Boolean {
		if (other == null || other !is Track)
			return false
		return trackId == other.trackId
	}
}

class SearchResponse(@SerializedName("results") val searchList: List<Track>)

interface ItunesApi {
	@GET("/search")
	fun search(@Query("term") text: String) : Call<SearchResponse>
}

