package com.example.playlistmaker.ui.playlist

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.playlist.model.PlaylistScreenState
import com.example.playlistmaker.presentation.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.ConfirmDialog
import com.example.playlistmaker.ui.PageHead
import com.example.playlistmaker.ui.PlaylistContent
import com.example.playlistmaker.ui.SearchContent
import com.example.playlistmaker.ui.theme.Black
import com.example.playlistmaker.ui.theme.LightGrey
import kotlinx.coroutines.launch

@SuppressLint("ServiceCast")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistCompose(
    modifier: Modifier = Modifier,
    playlist: Playlist,
    playlistViewModel: PlaylistViewModel,
    clickListener: (Track) -> Unit,
    editPlaylistListener: () -> Unit,
    onBackPressed: () -> Unit
){
    val context = LocalContext.current
    val density = LocalDensity.current
    var distanceToBottom by remember { mutableStateOf(0.dp) }

    val playlistState by playlistViewModel.stateLiveData.observeAsState()
    var playlist by remember { mutableStateOf(playlist) }
    var tracks by remember { mutableStateOf(listOf<Track>()) }

    val sharingString = "${playlist.name}\n${playlist.description}\n" +
            pluralStringResource(id = R.plurals.track_plurals, count = playlist.trackCount,
                playlist.trackCount) + "\n"

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()

    val fixedBottomSheetState = rememberBottomSheetScaffoldState()
    var shouldShowBottomSheet by remember { mutableStateOf(false) }

    var isTrackDeleteAlertDialog by rememberSaveable { mutableStateOf(false) }
    var isPlaylistDeleteAlertDialog by rememberSaveable { mutableStateOf(false) }

    var trackToDelete: Track? by remember { mutableStateOf(null) }

    when (playlistState) {
        is PlaylistScreenState.Content -> {
           tracks = (playlistState as PlaylistScreenState.Content).tracks
           if (tracks.isEmpty())
               Toast.makeText(context, context.getString(R.string.playlist_empty_toast),
                   Toast.LENGTH_SHORT).show()
        }
        is PlaylistScreenState.Init -> {
            playlist = (playlistState as PlaylistScreenState.Init).playlist
        }
    }

    Box(modifier = Modifier.fillMaxSize(1f))
    {
        ConfirmDialog(
            visible = isTrackDeleteAlertDialog,
            title = stringResource(R.string.track_delete_dialog_title),
            text = "",
            okButton = stringResource(R.string.track_delete_dialog_ok),
            cancelButton = stringResource(R.string.track_delete_dialog_cancel),
            onDismissRequest = { isTrackDeleteAlertDialog = false },
            onConfirmation = {
                isTrackDeleteAlertDialog = false
                trackToDelete?.let {
                    playlistViewModel.deleteTrackFromPlaylist(it)
                    playlistViewModel.updateData()
                }
            },
        )

        ConfirmDialog(
            visible = isPlaylistDeleteAlertDialog,
            title = stringResource(R.string.playlist_delete),
            text = stringResource(R.string.playlist_delete_dialog_message),
            okButton = stringResource(R.string.playlist_delete_dialog_ok),
            cancelButton = stringResource(R.string.playlist_delete_dialog_cancel),
            onDismissRequest = { isPlaylistDeleteAlertDialog = false },
            onConfirmation = {
                isPlaylistDeleteAlertDialog = false
                playlistViewModel.deletePlaylist()
                onBackPressed.invoke()
            },
        )

        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .background(color = LightGrey)
        )
        {
            Box(
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .aspectRatio(1f),
                    contentScale = ContentScale.FillHeight,
                    model = ImageRequest.Builder(context)
                        .data(playlist.coverUri)
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .build(),
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.baseline_gesture_24),
                    error = painterResource(R.drawable.baseline_gesture_24)
                )

                PageHead(isBackArrow = true) {
                    onBackPressed.invoke()
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(color = LightGrey)
                    .padding(0.dp, 16.dp, 0.dp, 0.dp),
                verticalArrangement = Arrangement.Top
            ) {

                Text(
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 4.dp),
                    text = playlist.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Black
                )
                if (!playlist.description.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(16.dp, 4.dp),
                        text = playlist.description!!,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Black
                    )
                }

                Text(
                    modifier = Modifier.padding(16.dp, 4.dp),
                    text = pluralStringResource(
                        id = R.plurals.time_plurals,
                        count = playlist.durationFormat.toInt(),
                        playlist.durationFormat.toInt()
                    )
                            + " · " + pluralStringResource(
                        id = R.plurals.track_plurals,
                        count = playlist.trackCount,
                        playlist.trackCount
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    color = Black
                )

                Row(modifier = Modifier.padding(16.dp, 8.dp).onGloballyPositioned {
                        coordinates ->
                    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    val screenHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val windowMetrics = windowManager.currentWindowMetrics
                        windowMetrics.bounds.height()
                    } else {
                        val display = windowManager.defaultDisplay
                        val size = Point()
                        display.getSize(size)
                        size.y
                    }
                    with(density) {
                        val elementBottom = coordinates.positionInWindow().y + coordinates.size.height
                        distanceToBottom = (screenHeight - elementBottom).toDp() - 64.dp
                    }
                }

                ) {
                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                sharingPlaylist(
                                    tracks,
                                    context,
                                    sharingString,
                                    playlistViewModel
                                )
                            },
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Black)
                    )
                    Image(
                        modifier = Modifier
                            .padding(16.dp, 0.dp)
                            .size(24.dp)
                            .clickable {
                                shouldShowBottomSheet = true
                                scope.launch { bottomSheetState.partialExpand() }
                            },
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Black)
                    )
                }

                TrackListBottomSheet(
                    bottomSheetScaffoldState = fixedBottomSheetState,
                    onDismissRequest = { },
                    hideBottomSheet = { },
                    tracks = tracks,
                    distanceToBottom = distanceToBottom,
                    clickListener = { track -> clickListener(track) },
                    longClickListener = { track ->
                            trackToDelete = track
                            isTrackDeleteAlertDialog = true
                    }
                )
            }

            PlaylistOptionsBottomSheet(
                visible = shouldShowBottomSheet,
                bottomSheetState = bottomSheetState,
                onDismissRequest = { shouldShowBottomSheet = false },
                hideBottomSheet = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            shouldShowBottomSheet = false
                        }
                    }
                },
                playlistList = listOf(playlist),
                deletePlaylistListener = {
                    isPlaylistDeleteAlertDialog = true
                },
                sharePlaylistListener = {
                    sharingPlaylist(tracks, context, sharingString, playlistViewModel)
                },
                editPlaylistListener = {
                    editPlaylistListener()
                }
            )
        }
    }
}

fun sharingPlaylist(tracks: List<Track>, context: Context,
                    sharingString: String, playlistViewModel: PlaylistViewModel) {
    if (tracks.isEmpty())
        Toast.makeText(context, context.getString(R.string.playlist_sharing_empty),
            Toast.LENGTH_SHORT).show()
    else {
        var number = 1
        var sharingString = sharingString
        tracks.forEach {
                track -> sharingString += "$number. ${track.artistName} - ${track.trackName} (${track.trackTimeFormat})\n"
            number++
        }
        playlistViewModel.sharingPlaylist(sharingString)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistOptionsBottomSheet(
    visible: Boolean,
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
    hideBottomSheet: () -> Unit,
    playlistList: List<Playlist>,
    deletePlaylistListener: () -> Unit,
    sharePlaylistListener: () -> Unit,
    editPlaylistListener: () -> Unit,
) {
    if (visible) {
        ModalBottomSheet(
            modifier = Modifier.padding(0.dp),
            onDismissRequest = { onDismissRequest() },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Column (modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp),
                    verticalArrangement = Arrangement.Top)
            {
                PlaylistContent(Modifier.padding(8.dp, 0.dp), playlistList) { }

                Text(
                    modifier = Modifier
                        .padding(0.dp, 16.dp)
                        .clickable {
                            sharePlaylistListener()
                        },
                    text = stringResource(R.string.playlist_sharing),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    modifier = Modifier.padding(0.dp, 16.dp)
                        .clickable {
                            editPlaylistListener()
                        },
                    text = stringResource(R.string.playlist_edit),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    modifier = Modifier.padding(0.dp, 16.dp)
                        .clickable{
                            deletePlaylistListener()
                        },
                    text = stringResource(R.string.playlist_delete),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackListBottomSheet(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onDismissRequest: () -> Unit,
    hideBottomSheet: () -> Unit,
    tracks: List<Track>,
    distanceToBottom: Dp,
    clickListener: (Track) -> Unit,
    longClickListener: (Track) -> Unit,
) {
    BottomSheetScaffold(
        modifier = Modifier.padding(0.dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = distanceToBottom,
        sheetSwipeEnabled = false,
        sheetContainerColor = MaterialTheme.colorScheme.primary,
        sheetContent = {
            SearchContent(
                Modifier.padding(8.dp, 0.dp),
                tracks,
                { track -> clickListener(track) },
                { track -> longClickListener(track) }
            )
        },
        content = {}
    )
}





