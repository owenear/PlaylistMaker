package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.SearchRequest
import com.example.playlistmaker.data.search.dto.SearchResponse
import com.example.playlistmaker.util.mappers.TrackMapper
import com.example.playlistmaker.domain.search.api.TrackRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl (private val networkClient: NetworkClient,
						   private val trackMapper: TrackMapper
) : TrackRepository {

	override fun searchTrack(query: String): Flow<Resource<List<Track>>> = flow {
		val response = networkClient.doRequest(SearchRequest(query))
		when (response.resultCode) {
			200 -> {
				if ((response as SearchResponse).results.isEmpty())
					emit(Resource.Error(404))
				else emit(Resource.Success((response as SearchResponse).results.map {
					trackMapper.map(it)
				}))
			}
			else -> {
				emit(Resource.Error(-1))
			}
		}
	}

}