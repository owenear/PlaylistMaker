package com.example.playlistmaker.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.presentation.playlists.models.PlaylistsScreenState
import com.example.playlistmaker.presentation.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.ErrorContent
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme

@Composable
fun Playlists(modifier: Modifier = Modifier, playlistsViewModel: PlaylistsViewModel,
              buttonClickListener: () -> Unit,
              playlistClickListener: (Playlist) -> Unit) {

    val playlistsState by playlistsViewModel.stateLiveData.observeAsState()

    Box(modifier = Modifier.fillMaxSize(1f)) {
        Column(modifier = Modifier
            .fillMaxWidth(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp),
                shape = RoundedCornerShape(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(8.dp, 0.dp),
                onClick = { buttonClickListener.invoke() },
                content = {
                    Text(
                        modifier = Modifier.padding(0.dp, 0.dp),
                        text = stringResource(R.string.library_new_playlist),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
            )

            when (playlistsState) {
                is PlaylistsScreenState.Content -> {
                    PlaylistsContent((playlistsState as PlaylistsScreenState.Content).playlists){
                        playlist -> playlistClickListener(playlist) }
                }
                else -> {
                    ErrorContent(Modifier.padding(0.dp, 44.dp, 0.dp, 0.dp),
                        painterResource(id = R.drawable.ic_nothing_found),
                        stringResource(id = R.string.library_playlists_default)
                    )
                }
            }
        }
    }
}


@Composable
fun PlaylistsContent(playlists: List<Playlist>, clickListener: (Playlist) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
            .padding(0.dp, 16.dp, 0.dp, 0.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        items(playlists) { playlist ->
            PlaylistItem(playlist) { clickListener(playlist) }
        }
    }
}


@Composable
fun PlaylistItem(playlist: Playlist, clickListener: (Playlist) -> Unit) {
    Column(modifier = Modifier
                .padding(4.dp, 4.dp)
                .fillMaxSize()
                .clickable { clickListener.invoke(playlist) }){
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(0.dp),
                contentScale = ContentScale.Crop,
                model = playlist.coverUri,
                contentDescription = null,
                placeholder = painterResource(R.drawable.baseline_gesture_24),
                error = painterResource(R.drawable.baseline_gesture_24)
            )
        }
        Column(Modifier.padding(0.dp, 0.dp),
            verticalArrangement = Arrangement.SpaceBetween) {
            Text(modifier = Modifier.height(16.dp),
                text = playlist.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onPrimary)
            Text(modifier = Modifier.height(16.dp),
                text = pluralStringResource(id = R.plurals.track_plurals,
                    count = playlist.trackCount,
                    playlist.trackCount),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}


@Preview
@Composable
fun PlaylistContentPreview() {
    val playlists = listOf<Playlist>(Playlist(id=1, name="play",
        description="", coverUri="file:///storage/emulated/0/Android/data/com.example.playlistmaker/files/Pictures/playlist_covers/1.jpg",
        trackCount=0, duration=0, durationFormat="0"),
        Playlist(id=1, name="play",
            description="", coverUri="file:///storage/emulated/0/Android/data/com.example.playlistmaker/files/Pictures/playlist_covers/1.jpg",
            trackCount=0, duration=0, durationFormat="0"),
        Playlist(id=1, name="play",
        description="", coverUri="file:///storage/emulated/0/Android/data/com.example.playlistmaker/files/Pictures/playlist_covers/1.jpg",
        trackCount=0, duration=0, durationFormat="0")
        )
    PlaylistMakerTheme {
        PlaylistsContent(playlists) {  }
    }
}