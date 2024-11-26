package com.example.playlistmaker.presentation.library.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouritesBinding
import com.example.playlistmaker.presentation.library.view_model.FavouritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private val favouritesViewModel by viewModel<FavouritesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        binding.placeholderTextView.text = getString(R.string.library_favourites_default)
        binding.placeholderImageView.setImageResource(R.drawable.ic_nothing_found)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavouritesFragment()
    }

}