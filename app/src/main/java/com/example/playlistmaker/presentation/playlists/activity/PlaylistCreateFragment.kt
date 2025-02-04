package com.example.playlistmaker.presentation.playlists.activity

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreateBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.presentation.App
import com.example.playlistmaker.presentation.playlists.models.PlaylistCreateScreenState
import com.example.playlistmaker.presentation.playlists.view_model.PlaylistCreateViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistCreateFragment: Fragment() {

    private var _binding: FragmentPlaylistCreateBinding? = null
    private val binding get() = _binding!!

    private var coverUri: Uri? = null

    private val playlist by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(PlaylistCreateFragment.ARGS_PLAYLIST, Playlist::class.java)
        } else {
            requireArguments().getSerializable(PlaylistCreateFragment.ARGS_PLAYLIST) as Playlist
        }
    }

    private val playlistCreateViewModel: PlaylistCreateViewModel by viewModel {
        parametersOf(playlist)
    }

    private val createConfirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialogTheme)
            .setTitle(getString(R.string.playlist_dialog_title))
            .setNegativeButton(getString(R.string.playlist_dialog_cancel)) { dialog, which -> }
            .setPositiveButton(getString(R.string.playlist_dialog_ok)) { dialog, which ->
                findNavController().navigateUp()
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPlaylistCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistToolbar.setNavigationOnClickListener {
            playlistCreateViewModel.onBackPressed()
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            uri -> if (uri != null) {
                    coverUri = uri
                    Log.d("VM STATE", coverUri.toString())
                    Glide.with(this)
                        .load(uri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy( DiskCacheStrategy.NONE )
                        .transform(CenterCrop(),RoundedCorners((8 * App.DISPLAY_DENSITY).toInt()))
                        .into(binding.coverImageView)
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
                binding.descriptionInputEditText.text.toString(), coverUri)
        }

        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                playlistCreateViewModel.onBackPressed()
            }
        })

    }

    private fun render(state: PlaylistCreateScreenState) {
        when (state) {
            is PlaylistCreateScreenState.Create -> showCreate()
            is PlaylistCreateScreenState.Update -> showUpdate(state.playlist)
            is PlaylistCreateScreenState.Disabled -> showDisabled()
            is PlaylistCreateScreenState.Enabled -> showEnabled()
            is PlaylistCreateScreenState.Created -> showCreated(state.playlistName)
            is PlaylistCreateScreenState.BackPressed -> showBackPressed(state.isPlaylistCreated)
        }
    }

    private fun showCreate() {
        binding.createPlaylistButton.text = getString(R.string.playlist_create_button)
    }

    private fun showUpdate(playlist: Playlist) {
        binding.createPlaylistButton.text = getString(R.string.playlist_update_button)
        binding.nameInputEditText.setText(playlist.name)
        binding.descriptionInputEditText.setText(playlist.description)
        coverUri = playlist.coverUri
        Glide.with(this)
            .load(playlist.coverUri)
            .skipMemoryCache(true)
            .diskCacheStrategy( DiskCacheStrategy.NONE )
            .transform(CenterCrop(),RoundedCorners((8 * App.DISPLAY_DENSITY).toInt()))
            .into(binding.coverImageView)
    }

    private fun showDisabled() = with(binding) {
        createPlaylistButton.isEnabled = false
    }

    private fun showEnabled() = with(binding) {
        createPlaylistButton.isEnabled = true
    }

    private fun showCreated(playlistName: String) {
        Toast.makeText(context, getString(R.string.playlist_create_success_toast, playlistName),
            Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun showBackPressed(isPlaylistCreated: Boolean) {
        if (isPlaylistCreated or (binding.nameInputEditText.text.isNullOrEmpty() and
            binding.descriptionInputEditText.text.isNullOrEmpty() and
                    (binding.coverImageView.drawable == null)))
            findNavController().navigateUp()
        else
            createConfirmDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        fun createArgs(playlist: Playlist? = null): Bundle = bundleOf(ARGS_PLAYLIST to playlist)
    }

}