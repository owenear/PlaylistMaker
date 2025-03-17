package com.example.playlistmaker.presentation.player.activity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.example.playlistmaker.presentation.playlists.activity.PlaylistCreateFragment
import com.example.playlistmaker.services.MusicService
import com.example.playlistmaker.services.NetworkBroadcastReceiver
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment() : Fragment() {

    private val networkBroadcastReceiver: BroadcastReceiver by inject()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            playerViewModel.setAudioPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playerViewModel.removeAudioPlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Toast.makeText(requireContext(), getString(R.string.player_service_cant_start),
                Toast.LENGTH_LONG).show()
        }
    }

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


    private fun bindMusicService() {
        if (trackItem != null) {
            val intent = Intent(requireContext(), MusicService::class.java).apply {
                putExtra(ARGS_TRACK, trackItem)
            }
            requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindMusicService() {
        if (trackItem != null) {
            requireContext().unbindService(serviceConnection)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.startNotification()
        requireContext().unregisterReceiver(networkBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.stopNotification()
        playerViewModel.updateData()
        ContextCompat.registerReceiver(
            requireContext(),
            networkBroadcastReceiver,
            IntentFilter(NetworkBroadcastReceiver.ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }

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

        binding.playerPlaybackButton.setOnClickListener{
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
                            findNavController().navigate(R.id.action_playerFragment_to_playlistCreateFragment,
                                PlaylistCreateFragment.createArgs(null))
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
        playerPlaybackButton.isEnabled = false
    }

    private fun showPrepared() = with(binding) {
        playerPlaybackButton.isEnabled = true
        playerPlaybackButton.setButtonImage(PlaybackButtonView.BUTTON_PLAY)
        timeTextView.text = getString(R.string.player_time_default)
    }

    private fun showPlaying(playTime: Int) = with(binding) {
        timeTextView.text = dateFormat.format(playTime)
    }

    private fun showPaused(pauseTime: Int) = with(binding) {
        timeTextView.text = dateFormat.format(pauseTime)
    }

    private fun showFavorite(isFavorite: Boolean) {
        if (isFavorite)
            binding.playerFavoriteButton.setImageResource(R.drawable.ic_favorite_button_true)
        else
            binding.playerFavoriteButton.setImageResource(R.drawable.ic_favorite_button_false)
    }

    override fun onDestroyView() {
        unbindMusicService()
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
        const val ARGS_TRACK = "trackItem"
        fun createArgs(trackItem: Track): Bundle = bundleOf(ARGS_TRACK to trackItem)
    }

}