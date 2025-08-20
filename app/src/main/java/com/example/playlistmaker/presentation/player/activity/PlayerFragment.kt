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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.example.playlistmaker.services.MusicService
import com.example.playlistmaker.services.NetworkBroadcastReceiver
import com.example.playlistmaker.ui.player.Player
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

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
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Player(
                            modifier = Modifier.padding(innerPadding),
                            trackItem, playerViewModel
                        ) { findNavController().navigateUp() }
                    }
                }
            }
        }
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

        requireActivity().onBackPressedDispatcher.addCallback(
            object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })

    }

    override fun onDestroyView() {
        unbindMusicService()
        super.onDestroyView()
    }

    companion object {
        const val ARGS_TRACK = "trackItem"
        fun createArgs(trackItem: Track): Bundle = bundleOf(ARGS_TRACK to trackItem)
    }

}