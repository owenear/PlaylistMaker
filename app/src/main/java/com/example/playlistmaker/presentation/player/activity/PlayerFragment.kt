package com.example.playlistmaker.presentation.player.activity

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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.App
import com.example.playlistmaker.presentation.player.models.PlayerScreenState
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment() : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val trackItem by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(ARGS_TRACK, Track::class.java)
        } else {
            requireArguments().getSerializable(ARGS_TRACK) as Track
        }
    }

    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(trackItem)
    }
    private lateinit var clickListenerDebounce: (Playlist) -> Unit

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val playerPlaylistAdapter by lazy {
        PlayerPlaylistAdapter() { playlist -> clickListenerDebounce(playlist) }
    }

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.playbackControl(true)
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.updateData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        clickListenerDebounce = debounce<Playlist>(CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false) {
                playlist -> playlistClickListener(playlist)
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.titleTextView.text = trackItem?.trackName
        binding.artistTextView.text = trackItem?.artistName

        binding.durationTextViewValue.text = trackItem?.trackTimeFormat
        binding.albumTextViewValue.text = trackItem?.collectionName
        binding.yearTextViewValue.text = trackItem?.releaseYear
        binding.genreTextViewValue.text = trackItem?.primaryGenreName
        binding.countryTextViewValue.text = trackItem?.country

        val trackInfoMap = mapOf(
            binding.albumTextViewValue to binding.albumTextViewGroup,
            binding.yearTextViewValue to binding.yearTextViewGroup,
            binding.genreTextViewValue to binding.genreTextViewGroup,
            binding.countryTextViewValue to binding.countryTextViewGroup
        )

        for ((textView, viewGroup) in trackInfoMap) {
            viewGroup.visibility = if (textView.text.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        val artworkUrl100 = trackItem?.artworkUrl100
        val coverURL = if (artworkUrl100.isNullOrEmpty()) R.drawable.baseline_gesture_24 else
            artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

        Glide.with(this)
            .load(coverURL)
            .placeholder(R.drawable.baseline_gesture_24)
            .centerCrop()
            .transform(RoundedCorners((8 * App.DISPLAY_DENSITY).toInt()))
            .into(binding.coverImageView)

        binding.playerPlayButton.setOnClickListener {
            playerViewModel.playbackControl()
        }

        binding.playerFavoriteButton.setOnClickListener {
            playerViewModel.onFavoriteClicked()
        }

        playerViewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.playlistRecyclerView.adapter = playerPlaylistAdapter

        binding.playerAddButton.setOnClickListener{
            playerViewModel.updateData()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        var isNewPlaylistButtonClicked = false

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        if (isNewPlaylistButtonClicked)
                            findNavController().navigate(R.id.action_playerFragment_to_playlistCreateFragment)
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

        binding.newPlaylistButton.setOnClickListener {
            isNewPlaylistButtonClicked = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })

    }

    private fun playlistClickListener(playlist: Playlist) {
        playerViewModel.onPlaylistClicked(playlist)
    }

    private fun render(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Default -> showDefault()
            is PlayerScreenState.Prepared -> showPrepared()
            is PlayerScreenState.Playing -> showPlaying(state.playTime)
            is PlayerScreenState.Paused -> showPaused(state.pauseTime)
            is PlayerScreenState.Favorite -> showFavorite(state.isFavorite)
            is PlayerScreenState.Playlists -> showPlaylists(state.playlists)
            is PlayerScreenState.AddResult -> showAddResult(state.isTrackInPlaylist, state.playlist)
        }
    }

    private fun showAddResult(isTrackInPlaylist: Boolean, playlist: Playlist) {
        if (isTrackInPlaylist) {
            Toast.makeText(context, getString(R.string.playlist_track_add_fail_toast, playlist.name),
                Toast.LENGTH_SHORT).show()
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            Toast.makeText(context, getString(R.string.playlist_track_add_success_toast, playlist.name),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        playerPlaylistAdapter.items = playlists
    }

    private fun showDefault() = with(binding) {
        playerPlayButton.isEnabled = false
        playerPlayButton.setBackgroundResource(R.drawable.ic_play_button)
    }

    private fun showPrepared() = with(binding) {
        playerPlayButton.isEnabled = true
        playerPlayButton.setBackgroundResource(R.drawable.ic_play_button)
        timeTextView.text = getString(R.string.player_time_default)
    }

    private fun showPlaying(playTime: Int) = with(binding) {
        playerPlayButton.setBackgroundResource(R.drawable.ic_pause_button)
        timeTextView.text = dateFormat.format(playTime)
    }

    private fun showPaused(pauseTime: Int) = with(binding) {
        playerPlayButton.setBackgroundResource(R.drawable.ic_play_button)
        timeTextView.text = dateFormat.format(pauseTime)
    }

    private fun showFavorite(isFavorite: Boolean) {
        if (isFavorite)
            binding.playerFavoriteButton.setImageResource(R.drawable.ic_favorite_button_true)
        else
            binding.playerFavoriteButton.setImageResource(R.drawable.ic_favorite_button_false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
        private const val ARGS_TRACK = "trackItem"
        fun createArgs(trackItem: Track): Bundle = bundleOf(ARGS_TRACK to trackItem)
    }

}