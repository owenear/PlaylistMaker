package com.example.playlistmaker.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState


@Composable
fun Settings(modifier: Modifier = Modifier, settingsViewModel: SettingsViewModel) {
    val practicumAndroidLink = stringResource(R.string.practicum_android_link)
    val emailStudent = stringResource(R.string.email_student)
    val emailSubject = stringResource(R.string.email_subject)
    val emailMessage = stringResource(R.string.email_message)
    val practicumOfferLink = stringResource(R.string.practicum_offer_link)

    Column(modifier = Modifier.fillMaxWidth(1f).padding(16.dp, 0.dp)) {
        Box(modifier = Modifier.height(56.dp)) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.settings_button),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Column(modifier = Modifier.padding(0.dp, 24.dp).fillMaxWidth(1f)) {
            SettingRowSwitch(stringResource(R.string.settings_night), settingsViewModel)
            SettingsRowImage(stringResource(R.string.settings_share),
                painterResource(id = R.drawable.share)
            ) { settingsViewModel.shareApp(practicumAndroidLink) }
            SettingsRowImage(stringResource(R.string.settings_support),
                painterResource(id = R.drawable.support)
            ) {
                settingsViewModel.openSupport(emailStudent, emailSubject, emailMessage)
            }
            SettingsRowImage(stringResource(R.string.settings_agreement),
                painterResource(id = R.drawable.arrow_forward)
            ) {
                settingsViewModel.openTerms(practicumOfferLink)
            }
        }
    }
}

@Composable
fun SettingRowSwitch(text: String, settingsViewModel: SettingsViewModel){
    val checked by settingsViewModel.nightThemeLiveData.observeAsState(false)
    Row(modifier = Modifier.fillMaxWidth().height(62.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                settingsViewModel.setTheme(!checked)
            },
            colors = SwitchDefaults.colors(
                checkedBorderColor = MaterialTheme.colorScheme.secondary,
                checkedThumbColor = MaterialTheme.colorScheme.onSecondary,
                checkedTrackColor = MaterialTheme.colorScheme.secondary,
                uncheckedBorderColor = MaterialTheme.colorScheme.secondary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSecondary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@Composable
fun SettingsRowImage(text: String, painter: Painter, clickListener: () -> Unit){
    Row(modifier = Modifier.fillMaxWidth().height(62.dp)
        .clickable(onClick = { clickListener.invoke() }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        Image(
            painter = painter,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
        )

    }
}

