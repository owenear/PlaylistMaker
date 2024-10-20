package com.example.playlistmaker.presentation.player.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.App
import com.example.playlistmaker.presentation.player.models.PlayerScreenState
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.search.models.SearchScreenState
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

	private lateinit var playButton: ImageButton
	private lateinit var timeTextView: TextView
	private val playerViewModel by lazy {
		ViewModelProvider(this,
		PlayerViewModel.getViewModelFactory())[PlayerViewModel::class.java]
	}
	private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

	override fun onPause() {
		super.onPause()
		playerViewModel.pausePlayer()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		setContentView(R.layout.activity_player)
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		val backButtonToolbar = findViewById<MaterialToolbar>(R.id.playerToolbar)
		backButtonToolbar.setNavigationOnClickListener {
			finish()
		}

		val titleTextView = findViewById<TextView>(R.id.titleTextView)
		val artistTextView = findViewById<TextView>(R.id.artistTextView)
		timeTextView = findViewById<TextView>(R.id.timeTextView)

		val durationTextView = findViewById<TextView>(R.id.durationTextViewValue)
		val albumTextView = findViewById<TextView>(R.id.albumTextViewValue)
		val yearTextView = findViewById<TextView>(R.id.yearTextViewValue)
		val genreTextView = findViewById<TextView>(R.id.genreTextViewValue)
		val countryTextView = findViewById<TextView>(R.id.countryTextViewValue)

		val albumGroup = findViewById<Group>(R.id.albumTextViewGroup)
		val yearGroup = findViewById<Group>(R.id.yearTextViewGroup)
		val genreGroup = findViewById<Group>(R.id.genreTextViewGroup)
		val countryGroup = findViewById<Group>(R.id.countryTextViewGroup)

		val coverImageView = findViewById<ImageView>(R.id.coverImageView)

		val trackItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent.getSerializableExtra("trackItem", Track::class.java)
		} else {
			intent.getSerializableExtra("trackItem") as Track
		}

		titleTextView.text = trackItem?.trackName
		artistTextView.text = trackItem?.artistName
		durationTextView.text = trackItem?.trackTimeFormat

		albumTextView.text = trackItem?.collectionName
		yearTextView.text = trackItem?.releaseYear
		genreTextView.text = trackItem?.primaryGenreName
		countryTextView.text = trackItem?.country

		val trackInfoMap = mapOf(
			albumTextView to albumGroup,
			yearTextView to yearGroup,
			genreTextView to genreGroup,
			countryTextView to countryGroup
		)

		for ((textView, viewGroup) in trackInfoMap) {
			viewGroup.visibility = if (textView.text.isNullOrEmpty()) View.GONE else View.VISIBLE
		}

		val artworkUrl100 = trackItem?.artworkUrl100
		val coverURL = if (artworkUrl100.isNullOrEmpty()) R.drawable.baseline_gesture_24 else
			artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
		val previewUrl = if (trackItem == null || trackItem.previewUrl.isNullOrEmpty())
			R.string.player_default_preview_url.toString() else trackItem.previewUrl

		Glide.with(this)
			.load(coverURL)
			.placeholder(R.drawable.baseline_gesture_24)
			.centerCrop()
			.transform(RoundedCorners((8 * App.DISPLAY_DENSITY).toInt()))
			.into(coverImageView)

		playButton = findViewById<ImageButton>(R.id.playerPlayButton)
		playerViewModel.preparePlayer(previewUrl)
		playButton.setOnClickListener {
			playerViewModel.playbackControl()
		}

		playerViewModel.stateLiveData.observe(this) { state ->
			render(state)
		}

		playerViewModel.playTimeLiveData.observe(this) { playTime ->
			timeTextView.text = dateFormat.format(playTime)
		}

	}

	private fun render(state: PlayerScreenState) {
		when (state) {
			is PlayerScreenState.Default -> showDefault()
			is PlayerScreenState.Prepared -> showPrepared()
			is PlayerScreenState.Playing -> showPlaying()
			is PlayerScreenState.Paused -> showPaused()
		}
	}

	private fun showDefault() {
		playButton.isEnabled = false
		playButton.setBackgroundResource(R.drawable.ic_play_button)
	}

	private fun showPrepared() {
		playButton.isEnabled = true
		playButton.setBackgroundResource(R.drawable.ic_play_button)
		timeTextView.text = getString(R.string.player_time_default)
	}

	private fun showPlaying() {
		playButton.setBackgroundResource(R.drawable.ic_pause_button)
	}

	private fun showPaused() {
		playButton.setBackgroundResource(R.drawable.ic_play_button)
	}

}