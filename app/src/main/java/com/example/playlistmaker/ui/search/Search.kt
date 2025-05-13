package com.example.playlistmaker.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.search.models.SearchScreenState
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.ErrorContent
import com.example.playlistmaker.ui.PageHead
import com.example.playlistmaker.ui.SearchContent
import com.example.playlistmaker.ui.theme.Blue
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme


@Composable
fun Search(modifier: Modifier = Modifier, searchViewModel: SearchViewModel,
           clickListener: (Track) -> Unit)  {

    val searchState by searchViewModel.stateLiveData.observeAsState()
    var searchQuery: String by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize(1f)) {

        Column(modifier = Modifier
            .fillMaxSize(1f)
            .padding(16.dp, 0.dp)) {

            PageHead(stringResource(id = R.string.search_button))

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(0.dp, 16.dp)
                    .onFocusChanged {
                        if (it.isFocused)
                            searchViewModel.processQuery(searchQuery)
                    },
                value = searchQuery,
                onValueChange = { newQuery ->
                    searchQuery = newQuery
                    searchViewModel.processQuery(searchQuery)
                },
                cursorBrush = SolidColor(Blue),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardActions = KeyboardActions(onDone = { searchViewModel.search(searchQuery)
                                    keyboardController?.hide() }),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(36.dp)
                            .background(
                                MaterialTheme.colorScheme.secondary,
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
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
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
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            innerTextField()
                        }
                        Box(modifier = Modifier
                            .padding(0.dp, 0.dp, 8.dp, 0.dp)
                            .width(24.dp)) {
                            if (searchQuery.isNotBlank()) {
                                Image(
                                    modifier = Modifier.clickable(onClick = {
                                        searchQuery = ""
                                        searchViewModel.processQuery(searchQuery)
                                    }),
                                    painter = painterResource(id = R.drawable.baseline_close_24),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
                                )
                            }
                        }
                    }
                }
            )

            when (searchState) {
                is SearchScreenState.Loading -> Loading()
                is SearchScreenState.SearchContent -> {
                    SearchContent(Modifier, (searchState as SearchScreenState.SearchContent).trackList) {
                        track -> clickListener(track) }
                    keyboardController?.hide()
                }
                is SearchScreenState.HistoryContent -> {
                    HistoryContent((searchState as SearchScreenState.HistoryContent).trackList, {
                        searchViewModel.clearHistory() }) { track -> clickListener(track) }
                }
                is SearchScreenState.Error -> {
                    ErrorContent(Modifier, painterResource(id = R.drawable.ic_net_error),
                        stringResource(id = R.string.search_net_error))
                    { searchViewModel.search(searchQuery) }
                }
                is SearchScreenState.Empty -> {
                    ErrorContent(Modifier, painterResource(id = R.drawable.ic_nothing_found),
                        stringResource(id = R.string.search_nothing_found))
                }
                else -> return
            }
        }
    }
}

@Composable
fun Loading(){
    Box(Modifier
        .fillMaxHeight(0.3f)
        .fillMaxWidth(1f)) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(44.dp)
                .align(Alignment.BottomCenter),
            trackColor = Blue,
        )
    }
}

@Composable
fun HistoryContent(trackList: List<Track>,
                   clearHistoryClickListener: () -> Unit,
                   trackClickListener: (Track) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(0.dp, 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.height(52.dp)) {
            Text(modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(R.string.search_history),
                style = MaterialTheme.typography.titleLarge,)
        }
        SearchContent(Modifier
            .height(min((trackList.count()*62).dp, 310.dp)), trackList) {
            track -> trackClickListener(track) }
        Button(
            modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp),
            shape = RoundedCornerShape(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary
            ) ,
            contentPadding = PaddingValues(8.dp, 0.dp),
            onClick = { clearHistoryClickListener.invoke() },
            content = {
                Text(modifier = Modifier.padding(0.dp, 0.dp),
                    text = stringResource(R.string.search_history_clear_button),
                style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary)
            },
        )
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
    PlaylistMakerTheme {
        HistoryContent(trackList, {}) {}
    }
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

                PageHead(stringResource(id = R.string.search_button))

                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(0.dp, 16.dp),
                    value = searchQuery,
                    onValueChange = { newQuery ->
                        searchQuery = newQuery
                    },
                    cursorBrush = SolidColor(Blue),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .height(36.dp)
                                .background(
                                    MaterialTheme.colorScheme.secondary,
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
                                        color = MaterialTheme.colorScheme.onSecondary
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
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
                                    )
                                }
                            }
                        }

                    }
                )
                ErrorContent(Modifier, painterResource(id = R.drawable.ic_nothing_found),
                    stringResource(id = R.string.search_nothing_found))
            }
        }
    }
}
