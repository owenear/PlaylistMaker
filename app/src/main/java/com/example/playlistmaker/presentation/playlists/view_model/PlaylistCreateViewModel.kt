package com.example.playlistmaker.presentation.playlists.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlists.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.presentation.playlists.models.PlaylistCreateScreenState
import kotlinx.coroutines.launch

class PlaylistCreateViewModel(private val playlistInteractor: PlaylistInteractor,
    private val playlist: Playlist? = null) : ViewModel() {

    private val stateMutableLiveData = MutableLiveData<PlaylistCreateScreenState>(
        PlaylistCreateScreenState.Disabled)
    val stateLiveData : LiveData<PlaylistCreateScreenState> = stateMutableLiveData

    init {
        if (playlist == null) renderState(PlaylistCreateScreenState.Create)
        else renderState(PlaylistCreateScreenState.Update(playlist))
    }

    fun createPlaylist(playlistName: String,
                       playlistDescription: String?, playlistUri: Uri?) {
        viewModelScope.launch {
            playlistInteractor.createPlaylist(Playlist(playlist?.id, playlistName,
                playlistDescription, playlistUri))
        }
        renderState(PlaylistCreateScreenState.Created(playlistName))
    }

    fun onBackPressed() {
        renderState(PlaylistCreateScreenState.BackPressed(
            (stateLiveData.value is PlaylistCreateScreenState.Created)))
    }

    fun processInput(playlistName: String) {
        if (playlistName.isEmpty()) renderState(PlaylistCreateScreenState.Disabled)
        else renderState(PlaylistCreateScreenState.Enabled)
    }

    private fun renderState(state: PlaylistCreateScreenState) {
        stateMutableLiveData.postValue(state)
    }

}