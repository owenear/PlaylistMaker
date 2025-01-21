package com.example.playlistmaker.presentation.library.models

interface PlaylistCreateScreenState {

    data object Disabled : PlaylistCreateScreenState
    data object Enabled : PlaylistCreateScreenState
    data class Result(val playlistName: String) : PlaylistCreateScreenState

}