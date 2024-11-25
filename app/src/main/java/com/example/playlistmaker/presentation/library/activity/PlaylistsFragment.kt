package com.example.playlistmaker.presentation.library.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.presentation.library.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment()  {

    private lateinit var binding: FragmentPlaylistsBinding
    private val playlistViewModel by viewModel<PlaylistsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        binding.placeholderTextView.text = getString(R.string.library_playlists_default)
        binding.placeholderImageView.setImageResource(R.drawable.ic_nothing_found)
        return binding.root
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}