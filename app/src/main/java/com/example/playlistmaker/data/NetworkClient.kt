package com.example.playlistmaker.data
import com.example.playlistmaker.data.search.dto.Response

interface NetworkClient {
	fun doRequest(dto: Any): Response
}