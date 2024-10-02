package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.SearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

	private val retrofit = Retrofit.Builder()
		.baseUrl(API_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()

	private val itunesApiService = retrofit.create(ItunesApi::class.java)

	override fun doRequest(dto: Any): Response {
		if (dto is SearchRequest) {
			try {
				val response = itunesApiService.search(dto.query).execute()
				val body = response.body() ?: Response()
				return body.apply { resultCode = response.code() }
			}
			catch (e: Exception) {
				return Response()
			}
		} else {
			return Response().apply { resultCode = 400 }
		}
	}

	companion object {
		const val API_URL = "https://itunes.apple.com"
	}

}