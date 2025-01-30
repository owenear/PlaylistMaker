package com.example.playlistmaker.presentation.favorites.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.favorites.models.FavoritesScreenState
import com.example.playlistmaker.presentation.favorites.view_model.FavoritesViewModel
import com.example.playlistmaker.presentation.player.activity.PlayerFragment
import com.example.playlistmaker.presentation.search.activity.SearchAdapter
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel: FavoritesViewModel by viewModel()

    private lateinit var clickListenerDebounce: (Track) -> Unit

    private val favoritesAdapter by lazy {
        SearchAdapter { trackItem -> clickListenerDebounce(trackItem) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        favoritesViewModel.checkFavorites()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoritesRecyclerView.adapter = favoritesAdapter

        clickListenerDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false) {
                trackItem -> trackListClickListener(trackItem)
        }

        favoritesViewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }

    }

    private fun render(state: FavoritesScreenState) {
        when (state) {
            is FavoritesScreenState.Empty -> showEmpty()
            is FavoritesScreenState.Content -> showContent(state.trackList)
        }
    }

    private fun showEmpty() = with(binding) {
        favoritesRecyclerView.visibility = View.GONE
        placeholderTextView.visibility = View.VISIBLE
        placeholderImageView.visibility = View.VISIBLE
    }

    private fun showContent(trackList: List<Track>) {
        favoritesAdapter.items = trackList
        with(binding) {
            favoritesRecyclerView.visibility = View.VISIBLE
            placeholderTextView.visibility = View.GONE
            placeholderImageView.visibility = View.GONE
        }
    }

    private fun trackListClickListener(trackItem: Track) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playerFragment,
            PlayerFragment.createArgs(trackItem))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }

}