package com.example.playlistmaker.presentation.playlist.activity

import android.annotation.SuppressLint
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.activity.PlayerFragment
import com.example.playlistmaker.presentation.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.presentation.playlists.activity.PlaylistCreateFragment
import com.example.playlistmaker.ui.playlist.PlaylistCompose
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlaylistFragment: Fragment()  {

    private lateinit var clickListenerDebounce: (Track) -> Unit

    private val playlist by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(ARGS_PLAYLIST, Playlist::class.java)
        } else {
            requireArguments().getSerializable(ARGS_PLAYLIST) as Playlist
        }
    }

    private val playlistViewModel: PlaylistViewModel by viewModel {
        parametersOf(playlist)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        PlaylistCompose(
                            modifier = Modifier.padding(innerPadding),
                            playlist!!, playlistViewModel,
                            { trackItem -> clickListenerDebounce(trackItem) },
                            {
                                findNavController().navigate(R.id.action_playlistFragment_to_playlistCreateFragment,
                                    PlaylistCreateFragment.createArgs(playlist))
                            }
                        )
                        { findNavController().navigateUp() }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListenerDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false) {
                trackItem -> trackClickListener(trackItem)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

    }

    private fun trackClickListener(trackItem: Track) {
        trackItem.previewUrl = trackItem.previewUrl.ifEmpty { getString(R.string.player_default_preview_url) }
        findNavController().navigate(R.id.action_playlistFragment_to_playerFragment,
            PlayerFragment.createArgs(trackItem))
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
        private const val ARGS_PLAYLIST = "playlist"
        fun createArgs(playlist: Playlist): Bundle = bundleOf(ARGS_PLAYLIST to playlist)
    }
}