package com.example.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.SearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

	private val retrofit = Retrofit.Builder()
		.baseUrl(API_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()

	private val itunesApiService = retrofit.create(ItunesApi::class.java)

	override fun doRequest(dto: Any): Response {
		if (!isConnected()) return Response().apply { resultCode = -1 }
		if (dto is SearchRequest) {
			val response = itunesApiService.search(dto.query).execute()
			val body = response.body() ?: Response()
			return body.apply { resultCode = response.code() }
		} else {
			return Response().apply { resultCode = 400 }
		}
	}

	private fun isConnected(): Boolean {
		val connectivityManager = context.getSystemService(
			Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
		if (capabilities != null) {
			when {
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
			}
		}
		return false
	}

	companion object {
		const val API_URL = "https://itunes.apple.com"
	}

}