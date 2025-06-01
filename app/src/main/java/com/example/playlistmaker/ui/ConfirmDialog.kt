package com.example.playlistmaker.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.playlistmaker.ui.theme.LightGrey

@Composable
fun ConfirmDialog(
    visible: Boolean,
    title: String,
    text: String,
    okButton: String,
    cancelButton: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = {
                onDismissRequest.invoke()
            },
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmation.invoke()
                    }
                ) {
                    Text(text = okButton)
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismissRequest.invoke() }
                ) {
                    Text(text = cancelButton)
                }
            },
            containerColor = LightGrey,
        )
    }
}