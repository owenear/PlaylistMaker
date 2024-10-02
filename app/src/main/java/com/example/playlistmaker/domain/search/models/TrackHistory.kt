package com.example.playlistmaker.domain.search.models

data class TrackHistory (val trackList: MutableList<Track> = mutableListOf<Track>()) {

	fun addTrack(item: Track) {
		if (item in trackList) trackList.remove(item)
		trackList.add(0, item)
		if (trackList.size > 10) trackList.removeAt(10)
	}

	fun clear() {
		trackList.clear()
	}

	fun isEmpty() : Boolean {
		return trackList.isEmpty()
	}

}