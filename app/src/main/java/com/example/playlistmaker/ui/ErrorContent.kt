package com.example.playlistmaker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R

@Composable
fun ErrorContent(modifier: Modifier = Modifier, image: Painter, text: String,
                 updateClickListener: (() -> Unit)? = null) {
    Column(modifier = modifier
        .fillMaxWidth(1f)
        .padding(0.dp, 98.dp, 0.dp, 0.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(120.dp),
            painter = image,
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(0.dp, 16.dp),
            text = text,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        if (updateClickListener != null)
            Button(
                shape = RoundedCornerShape(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ) ,
                contentPadding = PaddingValues(8.dp, 0.dp),
                onClick = { updateClickListener.invoke() },
                content = {
                    Text(modifier = Modifier.padding(0.dp, 0.dp),
                        text = stringResource(R.string.search_update_button),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary)
                },
            )
    }
}