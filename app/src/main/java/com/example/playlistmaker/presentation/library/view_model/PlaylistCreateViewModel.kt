package com.example.playlistmaker.presentation.library.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlist.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.presentation.library.models.PlaylistCreateScreenState
import kotlinx.coroutines.launch

class PlaylistCreateViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val stateMutableLiveData = MutableLiveData<PlaylistCreateScreenState>(
        PlaylistCreateScreenState.Disabled)
    val stateLiveData : LiveData<PlaylistCreateScreenState> = stateMutableLiveData

    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect{
                result -> Log.d("VM STATE",result.toString())
            }
        }
    }

    fun createPlaylist(playlistName: String, playlistDescription: String, playlistUri: String) {
        viewModelScope.launch {
            playlistInteractor.createPlaylist(Playlist(null, playlistName,
                playlistDescription, playlistUri))
        }
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