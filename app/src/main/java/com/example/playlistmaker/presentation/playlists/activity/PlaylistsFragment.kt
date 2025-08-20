package com.example.playlistmaker.presentation.playlists.activity

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.presentation.playlist.activity.PlaylistFragment
import com.example.playlistmaker.presentation.playlists.models.PlaylistsScreenState
import com.example.playlistmaker.presentation.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker.ui.library.Favorites
import com.example.playlistmaker.ui.library.Playlists
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment()  {

    private val playlistsViewModel by viewModel<PlaylistsViewModel>()

    private lateinit var playlistClickListenerDebounce: (Playlist) -> Unit
    //private lateinit var buttonClickListener: () -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Playlists(modifier = Modifier.padding(innerPadding), playlistsViewModel,
                            { buttonClickListener() }) {
                                playlist -> playlistClickListenerDebounce(playlist)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        playlistsViewModel.updateData()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistClickListenerDebounce = debounce<Playlist>(CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false) {
                trackItem -> playlistClickListener(trackItem)
        }

    }

    private fun buttonClickListener() {
        findNavController().navigate(R.id.action_libraryFragment_to_PlaylistCreateFragment,
            PlaylistCreateFragment.createArgs(null))
    }

    private fun playlistClickListener(playlist: Playlist) {
        findNavController().navigate(R.id.action_libraryFragment_to_playlistFragment,
            PlaylistFragment.createArgs(playlist))
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }

}