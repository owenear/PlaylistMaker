package com.example.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.SearchRequest

class RetrofitNetworkClient(private val context: Context,
							private val itunesApiService: ItunesApi) : NetworkClient {

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

}