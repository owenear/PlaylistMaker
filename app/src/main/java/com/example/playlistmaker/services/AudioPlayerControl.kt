package com.example.playlistmaker.services

import com.example.playlistmaker.presentation.player.models.PlayerScreenState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerScreenState>
    fun startPlayer()
    fun pausePlayer()
    fun startNotification()
    fun stopNotification()
}