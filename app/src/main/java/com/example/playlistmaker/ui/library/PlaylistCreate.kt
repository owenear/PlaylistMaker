package com.example.playlistmaker.ui.library

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.playlists.models.PlaylistCreateScreenState
import com.example.playlistmaker.presentation.playlists.view_model.PlaylistCreateViewModel
import com.example.playlistmaker.ui.ConfirmDialog
import com.example.playlistmaker.ui.PageHead
import com.example.playlistmaker.ui.theme.Blue
import com.example.playlistmaker.ui.theme.Grey
import com.example.playlistmaker.ui.theme.White

@Composable
fun PlaylistCreate(modifier: Modifier = Modifier,
                   playlistCreateViewModel: PlaylistCreateViewModel,
                   onFinish: () -> Unit) {

    val context = LocalContext.current
    val playlistCreateState by playlistCreateViewModel.stateLiveData.observeAsState()
    var isAlertDialog by rememberSaveable { mutableStateOf(false) }

    var coverUri by remember { mutableStateOf("") }
    val borderColor = MaterialTheme.colorScheme.onSecondary
    var playlistName = ""
    var playlistDescription = ""

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        uri -> if (uri != null) {
            coverUri = uri.toString()
        }
    }

    when (playlistCreateState) {
        is PlaylistCreateScreenState.BackPressed -> {
            if ((playlistCreateState as PlaylistCreateScreenState.BackPressed).isPlaylistCreated or
                (playlistName.isBlank() and playlistDescription.isBlank() and coverUri.isBlank()))
                onFinish.invoke()
            else isAlertDialog = true
        }
        is PlaylistCreateScreenState.Update -> {
            val playlist = (playlistCreateState as PlaylistCreateScreenState.Update).playlist
            playlistName = playlist.name
            if (!playlist.coverUri.isNullOrEmpty())
                coverUri = playlist.coverUri.toString()
            if (!playlist.description.isNullOrEmpty()) playlistDescription = playlist.description
        }
        is PlaylistCreateScreenState.Created -> {
            Toast.makeText(context,
                stringResource(R.string.playlist_create_success_toast, playlistName),
                Toast.LENGTH_SHORT).show()
            onFinish.invoke()
        }
        is PlaylistCreateScreenState.Updated -> {
            Toast.makeText(context, stringResource(R.string.playlist_update_success_toast, playlistName),
                Toast.LENGTH_SHORT).show()
            onFinish.invoke()
        }
        is PlaylistCreateScreenState.Create  -> {       }
        else -> return
    }

    Box(modifier = Modifier.fillMaxSize(1f)) {

        ConfirmDialog(
            visible = isAlertDialog,
            title = stringResource(R.string.playlist_dialog_title),
            text = "",
            okButton = stringResource(R.string.playlist_dialog_ok),
            cancelButton = stringResource(R.string.playlist_dialog_cancel),
            onDismissRequest = { isAlertDialog = false },
            onConfirmation = {
                isAlertDialog = false
                onFinish.invoke()
            },
        )

        Column() {

            PageHead(stringResource(id = R.string.library_new_playlist), true) {
                playlistCreateViewModel.onBackPressed()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(16.dp, 0.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp)
                        .aspectRatio(1f)
                        .clickable {
                            pickMedia.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                        .drawBehind {
                            drawRoundRect(
                                color = borderColor,
                                style = Stroke(
                                    width = 2f,
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(50f, 50f),
                                        0f
                                    )
                                ),
                                cornerRadius = CornerRadius(8.dp.toPx())
                            )
                        }

                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .padding(0.dp),
                        contentScale = ContentScale.None,
                        model = ImageRequest.Builder(context)
                            .data(coverUri)
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .memoryCachePolicy(CachePolicy.DISABLED)
                            .build(),
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.ic_image_add),
                        error = painterResource(R.drawable.ic_image_add)
                    )
                }

                playlistName = playlistTextField(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    playlistName, stringResource(id = R.string.playlist_name_hint)
                )

                playlistDescription = playlistTextField(
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp)
                        .height(56.dp),
                    playlistDescription, stringResource(id = R.string.playlist_description_hint)
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        modifier = Modifier
                            .padding(0.dp, 8.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Grey,
                            containerColor = Blue,
                            contentColor = White
                        ),
                        contentPadding = PaddingValues(8.dp, 0.dp),
                        onClick = {
                            playlistCreateViewModel.createPlaylist(
                                playlistName,
                                playlistDescription, coverUri
                            )
                        },
                        content = {
                            Text(
                                modifier = Modifier.padding(0.dp, 0.dp),
                                text = stringResource(R.string.playlist_create_button),
                                style = MaterialTheme.typography.titleSmall,
                                color = White
                            )
                        },
                        enabled = !playlistName.isBlank()
                    )
                }

            }
        }
    }
}

@Composable
fun playlistTextField(modifier: Modifier, text: String, label: String): String {
    var value by remember { mutableStateOf(text) }
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { if (value != it) value = it },
        singleLine = true,
        label = {  Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )},
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blue,
            focusedLabelColor = Blue,
            focusedPlaceholderColor = Blue,
            focusedSupportingTextColor = Blue,
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary
        )
    )
    return value
}

