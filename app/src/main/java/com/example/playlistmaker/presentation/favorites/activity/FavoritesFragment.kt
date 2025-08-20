package com.example.playlistmaker.presentation.favorites.activity

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
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.favorites.models.FavoritesScreenState
import com.example.playlistmaker.presentation.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker.presentation.player.activity.PlayerFragment
import com.example.playlistmaker.ui.library.Favorites
import com.example.playlistmaker.ui.search.Search
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModel()

    private lateinit var clickListenerDebounce: (Track) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Favorites(modifier = Modifier.padding(innerPadding), favoritesViewModel) {
                                trackItem -> clickListenerDebounce(trackItem)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        favoritesViewModel.checkFavorites()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListenerDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false) {
                trackItem -> trackListClickListener(trackItem)
        }

    }

    private fun trackListClickListener(trackItem: Track) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playerFragment,
            PlayerFragment.createArgs(trackItem))
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }

}