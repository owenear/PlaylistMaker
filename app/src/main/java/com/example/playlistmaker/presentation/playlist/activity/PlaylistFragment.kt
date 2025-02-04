package com.example.playlistmaker.presentation.playlist.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.activity.PlayerFragment
import com.example.playlistmaker.presentation.player.activity.PlayerPlaylistAdapter
import com.example.playlistmaker.presentation.playlist.model.PlaylistScreenState
import com.example.playlistmaker.presentation.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.presentation.playlists.activity.PlaylistCreateFragment
import com.example.playlistmaker.presentation.search.activity.SearchAdapter
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlaylistFragment: Fragment()  {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var trackToDelete: Track

    private val trackDeleteConfirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialogTheme)
            .setTitle(getString(R.string.track_delete_dialog_title))
            .setMessage(getString(R.string.track_delete_dialog_message))
            .setNegativeButton(getString(R.string.track_delete_dialog_cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.track_delete_dialog_ok)) { _, _ ->
                playlistViewModel.deleteTrackFromPlaylist(trackToDelete)
            }
    }

    private val playlistDeleteConfirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialogTheme)
            .setTitle(getString(R.string.playlist_delete))
            .setMessage(getString(R.string.playlist_delete_dialog_message))
            .setNegativeButton(getString(R.string.playlist_delete_dialog_cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.playlist_delete_dialog_ok)) { _, _ ->
                playlistViewModel.deletePlaylist()
                findNavController().navigateUp()
            }
    }

    private lateinit var clickListenerDebounce: (Track) -> Unit

    private val playlist by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(ARGS_PLAYLIST, Playlist::class.java)
        } else {
            requireArguments().getSerializable(ARGS_PLAYLIST) as Playlist
        }
    }

    private val playlistTracksAdapter by lazy {
        SearchAdapter({ track -> trackLongClickListener(track) })
        { track -> clickListenerDebounce(track)}
    }

    private val playlistAdapter by lazy { PlayerPlaylistAdapter() }

    private val playlistViewModel: PlaylistViewModel by viewModel {
        parametersOf(playlist)
    }

    private lateinit var bottomSheetTracksBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetPlaylistBehavior: BottomSheetBehavior<LinearLayout>

    override fun onResume() {
        super.onResume()
        playlistViewModel.updateData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistToolbar.setNavigationOnClickListener{
            findNavController().navigateUp()
        }

        Glide.with(this)
            .load(playlist?.coverUri)
            .skipMemoryCache(true)
            .diskCacheStrategy( DiskCacheStrategy.NONE )
            .placeholder(R.drawable.baseline_gesture_24)
            .into(binding.coverImageView)

        clickListenerDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false) {
                trackItem -> trackClickListener(trackItem)
        }

        bottomSheetTracksBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetPlaylistBehavior = BottomSheetBehavior.from(binding.playlistMenuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.playlistTracksRecyclerView.adapter = playlistTracksAdapter
        binding.playlistMenuRecyclerView.adapter = playlistAdapter

        binding.playlistShareButton.setOnClickListener {
            playlistSharing()
        }

        binding.playlistMenuButton.setOnClickListener {
            bottomSheetPlaylistBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        var isPlaylistDeleteButtonClicked = false
        bottomSheetPlaylistBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        if (isPlaylistDeleteButtonClicked)
                            playlistDeleteConfirmDialog.show()
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1)/2
            }
        })

        binding.playlistSharingTextView.setOnClickListener{
            playlistSharing()
        }

        binding.playlistEditTextView.setOnClickListener {
            findNavController().navigate(R.id.action_playlistFragment_to_playlistCreateFragment,
                PlaylistCreateFragment.createArgs(playlist))
        }

        binding.playlistDeleteTextView.setOnClickListener{
            isPlaylistDeleteButtonClicked = true
            bottomSheetPlaylistBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        playlistViewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

    }

    private fun playlistSharing(){
        if (playlistTracksAdapter.items.isEmpty())
            Toast.makeText(context, getString(R.string.playlist_sharing_empty),
                Toast.LENGTH_SHORT).show()
        else {
            var sharingString = "${playlist?.name}\n${playlist?.description}\n${playlist?.trackCount} " +
                    resources.getQuantityString(R.plurals.track_plurals, playlist?.trackCount!!, playlist?.trackCount!!) + "\n"
            var number = 1
            playlistTracksAdapter.items.forEach {
                    track -> sharingString += "$number. ${track.artistName} - ${track.trackName} (${track.trackTimeFormat})\n"
                number++
            }
            playlistViewModel.sharingPlaylist(sharingString)
        }
    }

    private fun trackClickListener(trackItem: Track) {
        trackItem.previewUrl = trackItem.previewUrl.ifEmpty { getString(R.string.player_default_preview_url) }
        findNavController().navigate(R.id.action_playlistFragment_to_playerFragment,
            PlayerFragment.createArgs(trackItem))
    }

    private fun trackLongClickListener(trackItem: Track): Boolean {
        trackToDelete = trackItem
        trackDeleteConfirmDialog.show()
        return true
    }

    private fun render(state: PlaylistScreenState) {
        when (state) {
            is PlaylistScreenState.Init -> showInit(state.playlist)
            is PlaylistScreenState.Content -> showContent(state.tracks)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showInit(playlist: Playlist) = with(binding) {
        playlistNameTextView.text = playlist.name
        playlistDescriptionTextView.text = playlist.description
        playlistDurationTextView.text = playlist.durationFormat + " " +
                resources.getQuantityString(R.plurals.time_plurals,
                    playlist.durationFormat.toInt(), playlist.durationFormat.toInt()) +
                " · " + playlist.trackCount + " " +
                resources.getQuantityString(R.plurals.track_plurals,
                    playlist.trackCount, playlist.trackCount)
        playlistAdapter.items = listOf(playlist)
        if (playlistDescriptionTextView.text.isEmpty())
            playlistDescriptionTextView.visibility = View.GONE
        bottomSheetTracksBehavior.peekHeight = binding.playlist.height -
                binding.anchor.bottom
    }

    private fun showContent(tracks: List<Track>) {
        playlistTracksAdapter.items = tracks
        if (tracks.isEmpty())
            Toast.makeText(context, getString(R.string.playlist_empty_toast),
                Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
        private const val ARGS_PLAYLIST = "playlist"
        fun createArgs(playlist: Playlist): Bundle = bundleOf(ARGS_PLAYLIST to playlist)
    }
}