package com.example.playlistmaker.presentation.playlists.activity

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.presentation.playlists.view_model.PlaylistCreateViewModel
import com.example.playlistmaker.ui.library.PlaylistCreate
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistCreateFragment: Fragment() {

    private val playlist by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(ARGS_PLAYLIST, Playlist::class.java)
        } else {
            requireArguments().getSerializable(ARGS_PLAYLIST) as Playlist
        }
    }

    private val playlistCreateViewModel: PlaylistCreateViewModel by viewModel {
        parametersOf(playlist)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        PlaylistCreate(modifier = Modifier.padding(innerPadding),
                            playlistCreateViewModel
                        ) { findNavController().navigateUp() }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                playlistCreateViewModel.onBackPressed()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        fun createArgs(playlist: Playlist? = null): Bundle = bundleOf(ARGS_PLAYLIST to playlist)
    }

}