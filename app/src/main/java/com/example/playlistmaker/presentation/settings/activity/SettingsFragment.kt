package com.example.playlistmaker.presentation.settings.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.Settings
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Settings(modifier = Modifier.padding(innerPadding),
                            settingsViewModel = settingsViewModel)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}