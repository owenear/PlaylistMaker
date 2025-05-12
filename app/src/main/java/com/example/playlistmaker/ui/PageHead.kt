package com.example.playlistmaker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PageHead(pageName: String) {
    Box(modifier = Modifier.height(56.dp)) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = pageName,
            style = MaterialTheme.typography.titleLarge
        )
    }
}