package com.example.playlistmaker.domain.player.api


interface MediaPlayerInteractor {

	fun preparePlayer(dataSource: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)

	fun startPlayer()

	fun pausePlayer()

	fun releasePlayer()

	fun getPlayTime() : Int

}