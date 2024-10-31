package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.SearchRequest
import com.example.playlistmaker.data.search.dto.SearchResponse
import com.example.playlistmaker.util.mappers.TrackMapper
import com.example.playlistmaker.domain.search.api.TrackRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.Resource

class TrackRepositoryImpl (private val networkClient: NetworkClient,
						   private val trackMapper: TrackMapper
) : TrackRepository {

	override fun searchTrack(query: String): Resource<List<Track>> {
		val response = networkClient.doRequest(SearchRequest(query))
		return when (response.resultCode) {
			200 -> {
				if ((response as SearchResponse).results.isEmpty())
					return Resource.Error(404)
				Resource.Success((response as SearchResponse).results.map {
					trackMapper.map(it)
				})
			}
			else -> {
				Resource.Error(-1)
			}
		}
	}

}