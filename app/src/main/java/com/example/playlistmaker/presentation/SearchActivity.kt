package com.example.playlistmaker.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TrackHistory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText


class SearchActivity : AppCompatActivity() {

	private var searchString: String = SEARCH_STRING_DEF
	private var searchList = mutableListOf<Track>()

	private lateinit var searchAdapter: SearchAdapter
	private lateinit var historyAdapter: SearchAdapter
	private lateinit var trackHistory: TrackHistory

	private lateinit var tracksInteractor: TrackInteractor
	private lateinit var trackHistoryInteractor: TrackHistoryInteractor

	private lateinit var placeholderMessage: TextView
	private lateinit var placeholderImage: ImageView
	private lateinit var updateButton: Button

	private val handler = Handler(Looper.getMainLooper())
	private val searchRunnable = Runnable { search() }
	private lateinit var progressBar: ProgressBar

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putString(SEARCH_STRING_KEY, searchString)
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		searchString = savedInstanceState.getString(SEARCH_STRING_KEY, SEARCH_STRING_DEF)
	}

	override fun onPause() {
		trackHistoryInteractor.saveHistory(trackHistory)
		super.onPause()
	}

	override fun onResume() {
		super.onResume()
		historyAdapter.notifyDataSetChanged()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_search)

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		tracksInteractor = Creator.provideTracksInteractor()
		trackHistoryInteractor = Creator.provideTrackHistoryInteractor(applicationContext)

		searchAdapter = SearchAdapter(searchList) { trackItem -> trackListClickListener(trackItem) }
		trackHistory = trackHistoryInteractor.getHistory()
		historyAdapter = SearchAdapter(trackHistory.trackList) { trackItem -> trackListClickListener(trackItem) }

		placeholderMessage = findViewById<TextView>(R.id.placeholderTextView)
		placeholderImage = findViewById<ImageView>(R.id.placeholderImageView)
		updateButton = findViewById<Button>(R.id.updateButton)
		progressBar = findViewById<ProgressBar>(R.id.progressBar)

		val clearSearchButton = findViewById<ImageView>(R.id.searchClearIcon)
		val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
		val backButtonToolbar = findViewById<MaterialToolbar>(R.id.searchToolbar)
		val inputEditText = findViewById<TextInputEditText>(R.id.searchInputEditText)

		val searchRecyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)
		val searchHistoryRecyclerView = findViewById<RecyclerView>(R.id.searchHistoryRecyclerView)
		val searchHistoryGroup = findViewById<Group>(R.id.searchHistoryGroup)

		searchRecyclerView.adapter = searchAdapter
		searchHistoryRecyclerView.adapter = historyAdapter

		if (savedInstanceState != null) inputEditText.setText(searchString)

		backButtonToolbar.setNavigationOnClickListener {
			finish()
		}

		clearSearchButton.setOnClickListener {
			inputEditText.setText(SEARCH_STRING_DEF)
			searchList.clear()
			searchAdapter.notifyDataSetChanged()
			showMessage()
			val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
			inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
		}

		inputEditText.setOnFocusChangeListener { view, hasFocus ->
			if (hasFocus) {
				historyAdapter.notifyDataSetChanged()
				searchHistoryGroup.visibility = if (trackHistory.isEmpty()) View.GONE else View.VISIBLE
				searchRecyclerView.visibility = View.GONE
			} else {
				searchHistoryGroup.visibility = View.GONE
				searchRecyclerView.visibility = View.VISIBLE
			}
		}

		inputEditText.addTextChangedListener(
			onTextChanged = { charSequence, _, _, _ ->
				clearSearchButton.visibility = if (charSequence.isNullOrEmpty()) View.GONE else View.VISIBLE
				if (inputEditText.hasFocus() && charSequence.isNullOrEmpty()) {
					historyAdapter.notifyDataSetChanged()
					searchHistoryGroup.visibility = if (trackHistory.isEmpty()) View.GONE else View.VISIBLE
					searchRecyclerView.visibility = View.GONE
					showMessage()
				} else {
					searchList.clear()
					searchAdapter.notifyDataSetChanged()
					searchHistoryGroup.visibility = View.GONE
					searchRecyclerView.visibility = View.VISIBLE
				}
			},
			afterTextChanged = { editable ->
				if (!editable.isNullOrEmpty()) {
					searchString = editable.toString()
					searchDebounce()
				} else {
					handler.removeCallbacks(searchRunnable)
				}
			}
		)

		inputEditText.setOnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_DONE && !inputEditText.text.isNullOrEmpty()) {
				searchString = inputEditText.text.toString()
				handler.removeCallbacks(searchRunnable)
				search()
			}
			false
		}

		updateButton.setOnClickListener {
			search()
		}

		clearHistoryButton.setOnClickListener {
			trackHistory.clear()
			historyAdapter.notifyDataSetChanged()
			searchHistoryGroup.visibility = View.GONE
			showMessage()
		}

	}

	private fun searchDebounce() {
		handler.removeCallbacks(searchRunnable)
		handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
	}

	private fun search() {
		if (searchString.isNotEmpty()) {
			showMessage()
			progressBar.visibility = View.VISIBLE
			tracksInteractor.searchTracks(searchString,
				object : TrackInteractor.TrackConsumer {
					override fun consume(foundTrack: List<Track>?) {
						handler.post { progressBar.visibility = View.GONE }

						when {
							foundTrack == null -> {
								handler.post {
									showMessage(
										getString(R.string.search_net_error),
										R.drawable.ic_net_error, View.VISIBLE
									)
								}
							}

							foundTrack.isEmpty() -> {
								handler.post {
									showMessage(
										getString(R.string.search_nothing_found),
										R.drawable.ic_nothing_found, View.GONE
									)
								}
							}

							else -> {
								searchList.addAll(foundTrack)
								handler.post { searchAdapter.notifyDataSetChanged() }
								showMessage()
							}

						}
					}
				}
			)
		}
	}

	private fun showMessage(text: String? = null, imgRes: Int? = null, updateButtonVisibility: Int = View.GONE) =
		if (text != null && imgRes != null) {
			placeholderMessage.visibility = View.VISIBLE
			placeholderImage.visibility = View.VISIBLE
			updateButton.visibility = updateButtonVisibility
			searchList.clear()
			searchAdapter.notifyDataSetChanged()
			placeholderMessage.text = text
			placeholderImage.setImageResource(imgRes)
		} else {
			placeholderMessage.visibility = View.GONE
			placeholderImage.visibility = View.GONE
 			updateButton.visibility = View.GONE
		}

	private fun trackListClickListener(trackItem: Track) {
		trackHistory.addTrack(trackItem)
		trackHistoryInteractor.saveHistory(trackHistory)
		val playerIntent = Intent(this, PlayerActivity::class.java)
		playerIntent.putExtra("trackItem",trackItem)
		this.startActivity(playerIntent)
	}

	companion object {
		const val SEARCH_STRING_KEY = "SEARCH_STRING"
		const val SEARCH_STRING_DEF = ""
		private const val SEARCH_DEBOUNCE_DELAY = 2000L
	}
}