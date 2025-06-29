package com.example.playlistmaker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlists.models.Playlist


@Composable
fun PlaylistContent(modifier: Modifier, playlistList: List<Playlist>, clickListener: (Playlist) -> Unit) {
    LazyColumn (modifier.fillMaxWidth(1f)) {
        items(playlistList) { playlist ->
            Row(
                Modifier
                    .fillMaxWidth(1f)
                    .height(62.dp)
                    .padding(0.dp, 0.dp)
                    .clickable {
                        clickListener.invoke(playlist)
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(44.dp)
                        .padding(0.dp),
                    model = playlist.coverUri,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.baseline_gesture_24)
                )
                Column(Modifier
                    .fillMaxWidth(0.9f)
                    .padding(8.dp, 0.dp),
                    verticalArrangement = Arrangement.SpaceBetween) {
                    Text(text = playlist.name,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onPrimary)
                    Text(text = playlist.trackCount.toString() + " " +
                            pluralStringResource(id = R.plurals.track_plurals,
                                count = playlist.trackCount),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onTertiary)
                }
            }
        }
    }
}