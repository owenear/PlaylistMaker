package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class Track (
	val trackName: String,
	val artistName: String,
	@SerializedName("trackTimeMillis") val trackTime: Int,
	val artworkUrl100: String
)

class SearchResponse(@SerializedName("results") val searchList: List<Track>)

interface ItunesApi {
	@GET("/search")
	fun search(@Query("term") text: String) : Call<SearchResponse>
}

