package com.example.playlistmaker.presentation.search.activity

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.activity.PlayerFragment
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.example.playlistmaker.services.NetworkBroadcastReceiver
import com.example.playlistmaker.ui.search.Search
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.util.debounce
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment()  {

    private val networkBroadcastReceiver: BroadcastReceiver by inject()

    private val searchViewModel by viewModel<SearchViewModel>()

    private lateinit var clickListenerDebounce: (Track) -> Unit


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Search(modifier = Modifier.padding(innerPadding), searchViewModel) {
                            trackItem -> clickListenerDebounce(trackItem)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            networkBroadcastReceiver,
            IntentFilter(NetworkBroadcastReceiver.ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(networkBroadcastReceiver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListenerDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false) {
                trackItem -> trackListClickListener(trackItem)
        }

    }

    private fun trackListClickListener(trackItem: Track) {
        trackItem.previewUrl = trackItem.previewUrl.ifEmpty { getString(R.string.player_default_preview_url) }
        searchViewModel.addToHistory(trackItem)
        findNavController().navigate(R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(trackItem))
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }

}