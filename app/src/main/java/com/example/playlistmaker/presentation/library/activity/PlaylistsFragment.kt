package com.example.playlistmaker.presentation.library.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.presentation.library.models.PlaylistsScreenState
import com.example.playlistmaker.presentation.library.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment()  {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlistViewModel by viewModel<PlaylistsViewModel>()

    private val playlistAdapter by lazy { PlaylistAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_PlaylistCreateFragment)
        }
        binding.playlistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistRecyclerView.adapter = playlistAdapter

        playlistViewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: PlaylistsScreenState) {
        when (state) {
            is PlaylistsScreenState.Empty -> showEmpty()
            is PlaylistsScreenState.Content -> showContent(state.playlists)
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        playlistAdapter.items = playlists
        with(binding) {
            placeholderTextView.visibility = View.GONE
            placeholderImageView.visibility = View.GONE
        }
    }

    private fun showEmpty() = with(binding){
        placeholderTextView.visibility = View.VISIBLE
        placeholderImageView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}