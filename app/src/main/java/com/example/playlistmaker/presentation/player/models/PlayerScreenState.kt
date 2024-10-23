package com.example.playlistmaker.presentation.player.models

interface PlayerScreenState {

	data object Default : PlayerScreenState
	data object Prepared : PlayerScreenState
	data object Playing : PlayerScreenState
	data object Paused : PlayerScreenState

}