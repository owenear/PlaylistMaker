package com.example.playlistmaker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R

@Composable
fun PageHead(pageName: String, isBackArrow: Boolean = false,
             clickListener: (() -> Unit)? = null) {
    Row(modifier = Modifier.height(56.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        if (isBackArrow) {
            Image(
                modifier = Modifier
                    .padding(0.dp, 0.dp)
                    .width(44.dp)
                    .clickable { clickListener?.invoke() },
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = null,
            )
        }
        Text(
            text = pageName,
            style = MaterialTheme.typography.titleLarge
        )
    }
}