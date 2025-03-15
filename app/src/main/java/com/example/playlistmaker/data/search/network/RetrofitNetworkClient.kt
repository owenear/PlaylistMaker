package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.SearchRequest
import com.example.playlistmaker.data.NetworkConnector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val itunesApiService: ItunesApi,
							private val networkConnector: NetworkConnector
) : NetworkClient {

	override suspend fun doRequest(dto: Any): Response {
		if (!networkConnector.isConnected()) return Response().apply { resultCode = -1 }
		if (dto !is SearchRequest) return Response().apply { resultCode = 400 }
		return withContext(Dispatchers.IO) {
			try {
				val response = itunesApiService.search(dto.query)
				response.apply { resultCode = 200 }
			} catch (e: Throwable) {
				Response().apply { resultCode = 500 }
			}
		}
	}

/*
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
*/
}