package com.example.playlistmaker.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.search.models.SearchScreenState
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme


@Composable
fun Search(modifier: Modifier = Modifier, searchViewModel: SearchViewModel)  {

    val searchState by searchViewModel.stateLiveData.observeAsState()
    var searchQuery: String by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize(1f)) {

        if (searchState is SearchScreenState.Loading) ProgressBar()

        Column(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(16.dp, 0.dp)) {

            Box(modifier = Modifier.height(56.dp)) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.search_button),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(0.dp, 24.dp),
                value = searchQuery,
                onValueChange = { newQuery ->
                    searchQuery = newQuery
                },
                cursorBrush = SolidColor(colorResource(R.color.blue)),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardActions = KeyboardActions(onDone = {
                    searchViewModel.search(searchQuery)
                }),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(36.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(percent = 10)
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(8.dp, 0.dp)
                                .width(24.dp),
                            painter = painterResource(id = R.drawable.search_14),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(0.dp, 0.dp, 8.dp, 0.dp)
                        ) {
                            if (searchQuery.isBlank())
                                Text(
                                    text = stringResource(id = R.string.search_button),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                            innerTextField()
                        }
                        Box(modifier = Modifier
                            .padding(0.dp, 0.dp, 8.dp, 0.dp)
                            .width(24.dp)) {
                            if (searchQuery.isNotBlank()) {
                                Image(
                                    modifier = Modifier.clickable(onClick = { searchQuery = "" }),
                                    painter = painterResource(id = R.drawable.baseline_close_24),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                                )
                            }
                        }
                    }

                }
            )

            if (searchState is SearchScreenState.SearchContent)
                SearchResult((searchState as SearchScreenState.SearchContent).trackList)


        }
    }
}

@Composable
fun ProgressBar(){
    Box(Modifier.fillMaxSize(1f)) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(44.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = colorResource(R.color.blue),
        )
    }
}


@Composable
fun SearchResult(trackList: List<Track>) {
    LazyColumn (Modifier.fillMaxWidth(1f)) {
        for (track in trackList) {
            item {
                Row(
                    Modifier.fillMaxWidth(1f).padding(0.dp, 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier.size(44.dp).padding(0.dp),
                        model = track.artworkUrl100,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.baseline_gesture_24)
                    )
                    Column(Modifier.fillMaxWidth(0.8f).padding(8.dp, 0.dp)) {
                        Text(text = track.trackName.toString(),
                            maxLines = 1)
                        Text(text = track.artistName.toString() + " · "
                                + track.trackTimeFormat.toString(),
                            maxLines = 1)
                    }
                    Image(
                        modifier = Modifier.width(24.dp),
                        painter = painterResource(id = R.drawable.arrow_forward),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun SearchResultPreview() {
    val trackList = listOf<Track>(
        Track(trackId=1565717400, trackName="White Birds (Owen Ear Remix)", artistName= "DJ Orion & J.Shore", trackTime=428000, trackTimeFormat= "07:08",
            artworkUrl100= "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/ce/e0/6b/cee06be2-2382-d008-54c5-a0312eb602e3/853564324750.png/100x100bb.jpg",
            previewUrl= "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/ee/be/87/eebe8733-463e-65fc-c4ff-f96ea9e0267e/mzaf_5473332786579373987.plus.aac.p.m4a",
            collectionName= "Brotherhood (Remixes, Pt. 1) - EP", releaseYear="2014", primaryGenreName= "Electronica", country= "USA", isFavorite=false),
        Track(trackId=1565718819, trackName= "Flight of the Birds (Owen Ear Remix)", artistName= "Eric Rigo", trackTime=290000, trackTimeFormat= "04:50",
            artworkUrl100= "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/6f/43/4f/6f434f8e-8827-dfa0-4ee7-93de99018796/853564269235.png/100x100bb.jpg",
            previewUrl= "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/32/31/34/3231342b-d7dc-1faf-ec62-b71e9a914d2a/mzaf_258319441300988437.plus.aac.p.m4a",
            collectionName= "Flight of the Birds - Single", releaseYear="2014", primaryGenreName= "Electronica", country= "USA", isFavorite=false)
    )
    SearchResult(trackList)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchPreview(){
    PlaylistMakerTheme {
        var searchQuery: String by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize(1f)) {

            Column(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(16.dp, 0.dp)) {

                Box(modifier = Modifier.height(56.dp)) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = R.string.search_button),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(0.dp, 24.dp),
                    value = searchQuery,
                    onValueChange = { newQuery ->
                        searchQuery = newQuery
                    },
                    cursorBrush = SolidColor(colorResource(R.color.blue)),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .height(36.dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    RoundedCornerShape(percent = 10)
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(8.dp, 0.dp)
                                    .width(24.dp),
                                painter = painterResource(id = R.drawable.search_14),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(0.dp, 0.dp, 8.dp, 0.dp)
                            ) {
                                if (searchQuery.isBlank())
                                    Text(
                                        text = stringResource(id = R.string.search_button),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onTertiary
                                    )
                                innerTextField()
                            }
                            Box(modifier = Modifier
                                .padding(0.dp, 0.dp, 8.dp, 0.dp)
                                .width(24.dp)) {
                                if (searchQuery.isNotBlank()) {
                                    Image(
                                        modifier = Modifier.clickable(onClick = { searchQuery = "" }),
                                        painter = painterResource(id = R.drawable.baseline_close_24),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                                    )
                                }
                            }
                        }

                    }
                )
            }
        }
    }
}
