package com.example.playlistmaker.domain.impl
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository

class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository) : MediaPlayerInteractor {

	override fun preparePlayer(
		dataSource: String,
		onPreparedListener: () -> Unit,
		onCompletionListener: () -> Unit
	) {
		mediaPlayerRepository.preparePlayer(dataSource, onPreparedListener, onCompletionListener)
	}

	override fun startPlayer() {
		mediaPlayerRepository.startPlayer()
	}

	override fun pausePlayer() {
		mediaPlayerRepository.pausePlayer()
	}

	override fun releasePlayer() {
		mediaPlayerRepository.releasePlayer()
	}

	override fun getPlayTime() : Int {
		return mediaPlayerRepository.getPlayTime()
	}

}