package com.example.playlistmaker.presentation.library.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreateBinding
import com.example.playlistmaker.presentation.App
import com.example.playlistmaker.presentation.library.models.PlaylistCreateScreenState
import com.example.playlistmaker.presentation.library.view_model.PlaylistCreateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistCreateFragment: Fragment() {

    private var _binding: FragmentPlaylistCreateBinding? = null
    private val binding get() = _binding!!

    private val playlistCreateViewModel: PlaylistCreateViewModel by viewModel()
    private var coverUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPlaylistCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            uri -> if (uri != null) {
                    coverUri = uri
                    Glide.with(this)
                        .load(uri)
                        .placeholder(R.drawable.baseline_gesture_24)
                        .transform(CenterCrop(),RoundedCorners((8 * App.DISPLAY_DENSITY).toInt()))
                        .into(binding.coverImageView)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.coverImageView.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.nameInputEditText.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                playlistCreateViewModel.processInput(charSequence.toString())
            }
        )

        playlistCreateViewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.createPlaylistButton.setOnClickListener {
            playlistCreateViewModel.createPlaylist(binding.nameInputEditText.text.toString(),
                binding.descriptionInputEditText.text.toString(),
                coverUri.toString())
        }

    }

    private fun render(state: PlaylistCreateScreenState) {
        when (state) {
            is PlaylistCreateScreenState.Disabled -> showDisabled()
            is PlaylistCreateScreenState.Enabled -> showEnabled()
            is PlaylistCreateScreenState.Result -> showCreated(state.playlistName)
        }
    }

    private fun showDisabled() = with(binding) {
        createPlaylistButton.isEnabled = false
    }

    private fun showEnabled() = with(binding) {
        createPlaylistButton.isEnabled = true
    }

    private fun showCreated(playlistName: String) {
        Toast.makeText(context, getString(R.string.playlist_create_success_toast, playlistName),
            Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistCreateFragment()
    }

}