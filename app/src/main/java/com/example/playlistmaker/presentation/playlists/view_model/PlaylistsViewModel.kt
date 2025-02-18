package com.example.playlistmaker.presentation.playlists.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlists.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.presentation.playlists.models.PlaylistsScreenState
import kotlinx.coroutines.launch


class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor):  ViewModel()  {

    private val stateMutableLiveData = MutableLiveData<PlaylistsScreenState>()
    val stateLiveData : LiveData<PlaylistsScreenState> = stateMutableLiveData

    init {
        updateData()
    }

    fun updateData() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                    playlists -> processResult(playlists)
            }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty())
            renderState(PlaylistsScreenState.Empty)
        else
            renderState(PlaylistsScreenState.Content(playlists))

    }

    private fun renderState(state: PlaylistsScreenState) {
        stateMutableLiveData.postValue(state)
    }

}