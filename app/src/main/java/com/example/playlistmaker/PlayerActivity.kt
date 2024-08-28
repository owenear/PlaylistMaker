package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
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


class PlayerActivity : AppCompatActivity() {

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
		val searchHistory = SearchHistory((applicationContext as App).sharedPreferences)

		titleTextView.text = searchHistory.historyList[0].trackName
		artistTextView.text = searchHistory.historyList[0].artistName
		durationTextView.text = searchHistory.historyList[0].getFormatTrackTime("mm:ss")

		albumTextView.text = searchHistory.historyList[0].collectionName
		yearTextView.text = searchHistory.historyList[0].getReleaseYear()
		genreTextView.text = searchHistory.historyList[0].primaryGenreName
		countryTextView.text = searchHistory.historyList[0].country

		val trackInfoMap = mapOf(
			albumTextView to albumGroup,
			yearTextView to yearGroup,
			genreTextView to genreGroup,
			countryTextView to countryGroup
		)

		for ((textView, viewGroup) in trackInfoMap) {
			viewGroup.visibility = if (textView.text.isNullOrEmpty()) View.GONE else View.VISIBLE
		}

		val artworkUrl100 = searchHistory.historyList[0].artworkUrl100
		val coverURL = if (artworkUrl100.isNullOrEmpty()) R.drawable.baseline_gesture_24 else
			artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

		Glide.with(this)
			.load(coverURL)
			.placeholder(R.drawable.baseline_gesture_24)
			.centerCrop()
			.transform(RoundedCorners(8))
			.into(coverImageView)

	}
}