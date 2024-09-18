package com.example.playlistmaker

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

	private lateinit var playButton: ImageButton
	private var mediaPlayer = MediaPlayer()
	private var playerState = STATE_DEFAULT
	private val mainHandler = Handler(Looper.getMainLooper())
	private lateinit var timeTextView: TextView
	private val playTime = updatePlayTime()
	private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

	override fun onPause() {
		super.onPause()
		pausePlayer()
	}

	override fun onDestroy() {
		super.onDestroy()
		mediaPlayer.release()
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

		val backButton = findViewById<ImageButton>(R.id.playerBackButton)
		backButton.setOnClickListener{
			val displaySearch = Intent(this, SearchActivity::class.java)
			startActivity(displaySearch)
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
			intent.getParcelableExtra("trackItem", Track::class.java)
		} else {
			intent.getParcelableExtra<Track>("trackItem")
		}

		titleTextView.text = trackItem?.trackName
		artistTextView.text = trackItem?.artistName
		durationTextView.text = trackItem?.getFormatTrackTime("mm:ss")

		albumTextView.text = trackItem?.collectionName
		yearTextView.text = trackItem?.getReleaseYear()
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

		Glide.with(this)
			.load(coverURL)
			.placeholder(R.drawable.baseline_gesture_24)
			.centerCrop()
			.transform(RoundedCorners((8 * App.DISPLAY_DENSITY).toInt()))
			.into(coverImageView)

		playButton = findViewById<ImageButton>(R.id.playerPlayButton)
		preparePlayer(trackItem)
		playButton.setOnClickListener {
			playbackControl()
		}
	}

	private fun preparePlayer(trackItem: Track?) {
		val previewUrl = if (trackItem?.previewUrl.isNullOrEmpty())
			R.string.player_default_preview_url.toString() else trackItem?.previewUrl
		mediaPlayer.setDataSource(previewUrl)
		mediaPlayer.prepareAsync()
		mediaPlayer.setOnPreparedListener {
			playButton.isEnabled = true
			playerState = STATE_PREPARED
		}
		mediaPlayer.setOnCompletionListener {
			playButton.setBackgroundResource(R.drawable.ic_play_button)
			playerState = STATE_PREPARED
			mainHandler.removeCallbacks(playTime)
			timeTextView.text = getString(R.string.player_time_default)
		}
	}

	private fun startPlayer() {
		mediaPlayer.start()
		playButton.setBackgroundResource(R.drawable.ic_pause_button)
		playerState = STATE_PLAYING
		mainHandler.post(playTime)
	}

	private fun pausePlayer() {
		mediaPlayer.pause()
		playButton.setBackgroundResource(R.drawable.ic_play_button)
		mainHandler.removeCallbacks(playTime)
		playerState = STATE_PAUSED
	}

	private fun playbackControl() {
		when(playerState) {
			STATE_PLAYING -> {
				pausePlayer()
			}
			STATE_PREPARED, STATE_PAUSED -> {
				startPlayer()
			}
		}
	}

	private fun updatePlayTime(): Runnable {
		return object : Runnable {
			override fun run() {
				timeTextView.text = dateFormat.format(mediaPlayer.currentPosition)
				mainHandler.postDelayed(this, PLAY_TIME_DELAY)
			}
		}
	}

	companion object {
		private const val STATE_DEFAULT = 0
		private const val STATE_PREPARED = 1
		private const val STATE_PLAYING = 2
		private const val STATE_PAUSED = 3
		private const val PLAY_TIME_DELAY = 500L
	}

}