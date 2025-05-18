package com.example.playlistmaker.ui.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.favorites.models.FavoritesScreenState
import com.example.playlistmaker.presentation.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.ErrorContent
import com.example.playlistmaker.ui.SearchContent


@Composable
fun Favorites(modifier: Modifier = Modifier, favoritesViewModel: FavoritesViewModel,
              clickListener: (Track) -> Unit) {

    val favoritesState by favoritesViewModel.stateLiveData.observeAsState()

    Box(modifier = Modifier.fillMaxSize(1f)) {

        when (favoritesState) {
            is FavoritesScreenState.Content -> {
                SearchContent(Modifier.padding(0.dp, 24.dp, 0.dp, 0.dp),
                    (favoritesState as FavoritesScreenState.Content).trackList) {
                    track -> clickListener(track) }
            }
            else -> {
                ErrorContent(Modifier.padding(0.dp, 108.dp, 0.dp, 0.dp),
                    painterResource(id = R.drawable.ic_nothing_found),
                    stringResource(id = R.string.library_favourites_default))
            }
        }

    }

}