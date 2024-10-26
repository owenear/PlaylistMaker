package com.example.playlistmaker.data.player
import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.api.MediaPlayerRepository


class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {

	override fun preparePlayer(dataSource: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
		mediaPlayer.setDataSource(dataSource)
		mediaPlayer.prepareAsync()
		mediaPlayer.setOnPreparedListener {
			onPreparedListener()
		}
		mediaPlayer.setOnCompletionListener {
			onCompletionListener()
		}
	}

	override fun startPlayer() {
		mediaPlayer.start()
	}

	override fun pausePlayer() {
		mediaPlayer.pause()
	}

	override fun releasePlayer() {
		mediaPlayer.release()
	}

	override fun getPlayTime() : Int {
		return mediaPlayer.currentPosition
	}

}