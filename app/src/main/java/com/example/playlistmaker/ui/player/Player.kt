package com.example.playlistmaker.ui.player

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.models.PlayerScreenState
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.PageHead
import com.example.playlistmaker.ui.PlaylistContent
import com.example.playlistmaker.ui.theme.Grey
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.ui.theme.Red
import com.example.playlistmaker.ui.theme.White
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Player(
    modifier: Modifier = Modifier,
    track: Track?,
    playerViewModel: PlayerViewModel,
    onBackPressed: () -> Unit
){
    val context = LocalContext.current
    val playerState by playerViewModel.stateLiveData.observeAsState()
    var isPrepared by remember { mutableStateOf(false) }
    var playTime by remember { mutableStateOf("00:00") }
    var isFavorite by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()
    val shouldShowBottomSheet = remember { mutableStateOf(false) }

    var playlists by remember { mutableStateOf(listOf<Playlist>()) }

    when (playerState) {
        is PlayerScreenState.Default -> { isPrepared = false }
        is PlayerScreenState.Prepared -> {
            isPrepared = true
            playTime = "00:00"
            isPlaying = false
        }
        is PlayerScreenState.Playing -> {
            isPlaying = true
            playTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format((playerState as PlayerScreenState.Playing).playTime)
        }
        is PlayerScreenState.Favorite -> {
            isFavorite = (playerState as PlayerScreenState.Favorite).isFavorite
        }
        is PlayerScreenState.Paused -> {
            isPlaying = false
            playTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format((playerState as PlayerScreenState.Paused).pauseTime)
        }
        is PlayerScreenState.Playlists -> {
            playlists = (playerState as PlayerScreenState.Playlists).playlists
            Log.d("VM STATE", playlists.toString())
        }
        is PlayerScreenState.AddResult -> {
            if ((playerState as PlayerScreenState.AddResult).isTrackInPlaylist)
                Toast.makeText(context, stringResource(R.string.playlist_track_add_fail_toast,
                    (playerState as PlayerScreenState.AddResult).playlist.name),
                    Toast.LENGTH_SHORT).show()
            else {
                LaunchedEffect(scope) {
                    bottomSheetState.hide()
                }
                shouldShowBottomSheet.value = false
                Toast.makeText(context, stringResource(R.string.playlist_track_add_success_toast,
                        (playerState as PlayerScreenState.AddResult).playlist.name
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize(1f)) {
        PageHead(isBackArrow = true) {
            onBackPressed.invoke()
        }

        Column(modifier = Modifier.fillMaxSize().weight(1f).padding(16.dp, 0.dp),
               verticalArrangement = Arrangement.Bottom
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 0.dp, 0.dp, 16.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .padding(0.dp)
                        .aspectRatio(1f)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit,
                    model = ImageRequest.Builder(context)
                        .data(track?.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg"))
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .build(),
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.baseline_gesture_24),
                    error = painterResource(R.drawable.baseline_gesture_24)
                )
            }
            Text(
                modifier = Modifier.padding(0.dp, 4.dp),
                text = track?.trackName.toString(),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                modifier = Modifier.padding(0.dp, 4.dp),
                text = track?.artistName.toString(),
                style = MaterialTheme.typography.titleSmall
            )

            Column(modifier = Modifier.padding(0.dp, 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AddButton(painterResource(id = R.drawable.baseline_add_to_24)) {
                        playerViewModel.updateData()
                        shouldShowBottomSheet.value = true
                        scope.launch { bottomSheetState.partialExpand() }
                    }
                    Image(
                        modifier = Modifier.clickable {
                            playerViewModel.playbackControl()
                        },
                        painter = if (isPlaying)
                            painterResource(id = R.drawable.ic_pause_button)
                        else
                            painterResource(id = R.drawable.ic_play_button),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                    AddButton(
                        painterResource(id = R.drawable.ic_favorite_button_false),
                        isFavorite
                    ) {
                        playerViewModel.onFavoriteClicked()
                    }
                }

                Text(
                    modifier = Modifier.padding(0.dp, 8.dp).fillMaxWidth(1f),
                    textAlign = TextAlign.Center,
                    text = playTime,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            TrackData(track)
        }

        PlaylistListBottomSheet(
            visible = shouldShowBottomSheet.value,
            bottomSheetState = bottomSheetState,
            onDismissRequest = { shouldShowBottomSheet.value = false },
            hideBottomSheet = {
                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        shouldShowBottomSheet.value = false
                    }
                }
            },
            playlistList = playlists,
            clickListener = {
                playlist -> playerViewModel.onPlaylistClicked(playlist)
            }
        )
    }
}


@Composable
fun TrackData(track: Track?){
    Column(modifier = Modifier,
        verticalArrangement = Arrangement.Bottom) {
        RowData(stringResource(R.string.player_duration), track!!.trackTimeFormat)
        if (track.collectionName != null)
            RowData(stringResource(R.string.player_album), track.collectionName.toString())
        if (track.releaseYear != null)
            RowData(stringResource(R.string.player_year), track.releaseYear)
        if (track.primaryGenreName != null)
            RowData(stringResource(R.string.player_genre), track.primaryGenreName)
        if (track.country!= null)
            RowData(stringResource(R.string.player_country), track.country)
    }

}

@Composable
fun RowData(property: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().height(32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.fillMaxWidth(0.3f),
            maxLines = 1,
            textAlign = TextAlign.Start,
            text = property,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiary
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            textAlign = TextAlign.End,
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
fun AddButton(painter: Painter, isFavorite: Boolean = false, clickListener: () -> Unit) {
    Box(modifier = Modifier.size(52.dp)
        .background(Grey, CircleShape)
    ) {
        Image(modifier = Modifier.align(Alignment.Center)
            .clickable { clickListener.invoke() },
            painter = if (isFavorite) painterResource(id = R.drawable.ic_favorite_button_true)
                else painter,
            contentDescription = null,
            colorFilter = if (isFavorite) ColorFilter.tint(Red) else ColorFilter.tint(White)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistListBottomSheet(
    visible: Boolean,
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
    hideBottomSheet: () -> Unit,
    playlistList: List<Playlist>,
    clickListener: (Playlist) -> Unit
) {
    if (visible)
        ModalBottomSheet(
            onDismissRequest = { onDismissRequest() },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            PlaylistContent(Modifier.padding(8.dp, 16.dp), playlistList) {
                playlist -> clickListener(playlist) }
        }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PlayerView() {

    val track = Track(trackId=1565717400, trackName="White Birds (Owen Ear Remix)", artistName= "DJ Orion & J.Shore", trackTime=428000, trackTimeFormat= "07:08",
    artworkUrl100= "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/ce/e0/6b/cee06be2-2382-d008-54c5-a0312eb602e3/853564324750.png/100x100bb.jpg",
    previewUrl= "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/ee/be/87/eebe8733-463e-65fc-c4ff-f96ea9e0267e/mzaf_5473332786579373987.plus.aac.p.m4a",
    collectionName= "Brotherhood (Remixes, Pt. 1) - EP", releaseYear="2014", primaryGenreName= "Electronica", country= "USA", isFavorite=false)

    PlaylistMakerTheme {
        Column(modifier = Modifier.fillMaxSize(1f)) {
            PageHead(isBackArrow = true)

            Column(modifier = Modifier.fillMaxSize().weight(1f).padding(16.dp, 0.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(0.dp, 16.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .padding(0.dp)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit,
                        model = track?.artworkUrl100,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.baseline_gesture_24),
                        error = painterResource(R.drawable.baseline_gesture_24)
                    )
                }
                    Text(
                        modifier = Modifier.padding(0.dp, 4.dp),
                        text = track?.trackName.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        modifier = Modifier.padding(0.dp, 4.dp),
                        text = track?.artistName.toString(),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Column(modifier = Modifier.padding(0.dp, 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AddButton(painterResource(id = R.drawable.baseline_add_to_24)) {}
                            Image(
                                painterResource(id = R.drawable.ic_pause_button),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                            AddButton(painterResource(id = R.drawable.ic_favorite_button_false)) {}
                        }

                        Text(
                            modifier = Modifier.padding(0.dp, 8.dp).fillMaxWidth(1f),
                            textAlign = TextAlign.Center,
                            text = "00:00",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                TrackData(track)
            }
        }
    }
}
