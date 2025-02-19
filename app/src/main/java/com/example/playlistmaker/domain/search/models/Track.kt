package com.example.playlistmaker.domain.search.models

import java.io.Serializable
import java.util.Objects

data class Track (
	val trackId: Int,
	val trackName: String?,
	val artistName: String?,
	val trackTime: Long,
	val trackTimeFormat: String,
	val artworkUrl100: String?,
	var previewUrl: String,
	val collectionName: String?,
	val releaseYear: String?,
	val primaryGenreName: String?,
	val country: String?,
	var isFavorite: Boolean = false
): Serializable {

	override fun hashCode(): Int {
		return Objects.hash(trackId)
	}

	override fun equals(other: Any?): Boolean {
		if (other == null || other !is Track)
			return false
		return trackId == other.trackId
	}

}