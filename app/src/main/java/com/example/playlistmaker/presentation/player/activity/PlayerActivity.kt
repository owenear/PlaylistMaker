package com.example.playlistmaker.presentation.player.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.App
import com.example.playlistmaker.presentation.player.models.PlayerScreenState
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

	private val trackItem by lazy {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent.getSerializableExtra(App.PLAYER_INTENT_EXTRA_KEY, Track::class.java)
		} else {
			intent.getSerializableExtra(App.PLAYER_INTENT_EXTRA_KEY) as Track
		}
	}

	private val playerViewModel: PlayerViewModel by viewModel {
		parametersOf(trackItem)
	}

	private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

	private lateinit var binding: ActivityPlayerBinding

	override fun onPause() {
		super.onPause()
		playerViewModel.playbackControl(true)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		binding = ActivityPlayerBinding.inflate(layoutInflater)
		setContentView(binding.root)
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		binding.playerToolbar.setNavigationOnClickListener {
			finish()
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

		playerViewModel.stateLiveData.observe(this) { state ->
			render(state)
		}

	}

	private fun render(state: PlayerScreenState) {
		when (state) {
			is PlayerScreenState.Default -> showDefault()
			is PlayerScreenState.Prepared -> showPrepared()
			is PlayerScreenState.Playing -> showPlaying(state.playTime)
			is PlayerScreenState.Paused -> showPaused(state.pauseTime)
			is PlayerScreenState.Favorite -> showFavorite(state.isFavorite)
		}
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

}