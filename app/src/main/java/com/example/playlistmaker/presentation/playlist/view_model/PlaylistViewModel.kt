package com.example.playlistmaker.presentation.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playlists.api.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.presentation.playlist.model.PlaylistScreenState
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlist: Playlist,
                        private val playlistInteractor: PlaylistInteractor,
                        private val sharingInteractor: SharingInteractor):  ViewModel() {

    private val stateMutableLiveData = MutableLiveData<PlaylistScreenState>()
    val stateLiveData : LiveData<PlaylistScreenState> = stateMutableLiveData

    init {
        updateData()
    }

    fun updateData(){
        viewModelScope.launch {
            playlistInteractor.getPlaylist(playlist).collect {
                playlistUpdated -> renderState(PlaylistScreenState.Init(playlistUpdated))
            }
            playlistInteractor.getTracksInPlaylist(playlist).collect {
                tracks -> renderState(PlaylistScreenState.Content(tracks))
            }
        }
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(track, playlist)
        }
        updateData()
    }

    fun deletePlaylist(){
        viewModelScope.launch {
            playlistInteractor.removePlaylist(playlist)
        }
    }

    fun sharingPlaylist(sharingString: String) {
        sharingInteractor.shareApp(sharingString)
    }

    private fun renderState(state: PlaylistScreenState) {
        stateMutableLiveData.postValue(state)
    }

}