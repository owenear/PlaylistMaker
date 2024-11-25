package com.example.playlistmaker.presentation.player.models

interface PlayerScreenState {

	data object Default : PlayerScreenState
	data object Prepared : PlayerScreenState
	data class Playing(val playTime: Int) : PlayerScreenState
	data class Paused(val pauseTime: Int) : PlayerScreenState
}