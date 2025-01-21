package com.example.playlistmaker.presentation.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.presentation.library.models.PlaylistCreateScreenState

class PlaylistCreateViewModel() : ViewModel() {

    private val stateMutableLiveData = MutableLiveData<PlaylistCreateScreenState>(
        PlaylistCreateScreenState.Disabled)
    val stateLiveData : LiveData<PlaylistCreateScreenState> = stateMutableLiveData


    fun createPlaylist(playlistName: String, playlistDescription: String) {
        renderState(PlaylistCreateScreenState.Result(playlistName))
    }

    fun processInput(playlistName: String) {
        if (playlistName.isEmpty()) renderState(PlaylistCreateScreenState.Disabled)
        else renderState(PlaylistCreateScreenState.Enabled)
    }

    private fun renderState(state: PlaylistCreateScreenState) {
        stateMutableLiveData.postValue(state)
    }

}