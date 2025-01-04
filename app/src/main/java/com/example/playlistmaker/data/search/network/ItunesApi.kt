package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
	@GET("/search")
	suspend fun search(@Query("term") text: String) : SearchResponse
}
