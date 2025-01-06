package com.example.playlistmaker.presentation.library.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorites.api.FavoriteInteractor
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.library.models.FavoritesScreenState
import kotlinx.coroutines.launch


class FavoritesViewModel(private val favoriteInteractor: FavoriteInteractor) :  ViewModel()  {

    private val stateMutableLiveData = MutableLiveData<FavoritesScreenState>(FavoritesScreenState.Empty)
    val stateLiveData : LiveData<FavoritesScreenState> = stateMutableLiveData

    init {
        checkFavorites()
    }

    fun checkFavorites() {
        viewModelScope.launch {
            favoriteInteractor.getFavorites().collect{
                    favorites -> processResult(favorites)
            }
        }
    }

    private fun processResult(favorites: List<Track>) {
        Log.d("VM STATE", favorites.toString())
        if (favorites.isEmpty()) {
            renderState(FavoritesScreenState.Empty)
        } else {
            renderState(FavoritesScreenState.Content(favorites))
        }
    }

    private fun renderState(state: FavoritesScreenState) {
        Log.d("VM STATE", state.toString())
        stateMutableLiveData.postValue(state)
    }
}