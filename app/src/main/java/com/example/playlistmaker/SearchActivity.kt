package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
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
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

	private var searchString: String = SEARCH_STRING_DEF
	private val retrofit = Retrofit.Builder()
		.baseUrl(API_URL)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
	private val itunesApiService = retrofit.create(ItunesApi::class.java)

	private var searchList = mutableListOf<Track>()
	private lateinit var searchAdapter: SearchAdapter
	private lateinit var searchHistory: SearchHistory
	private lateinit var historyAdapter: SearchAdapter

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
		searchHistory.save()
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

		searchHistory = SearchHistory((applicationContext as App).sharedPreferences)
		searchAdapter = SearchAdapter(searchList) { trackItem -> trackListClickListener(trackItem) }
		historyAdapter = SearchAdapter(searchHistory.historyList) { trackItem -> trackListClickListener(trackItem) }

		placeholderMessage = findViewById<TextView>(R.id.placeholderTextView)
		placeholderImage = findViewById<ImageView>(R.id.placeholderImageView)
		updateButton = findViewById<Button>(R.id.updateButton)

		progressBar = findViewById<ProgressBar>(R.id.progressBar)

		val clearSearchButton = findViewById<ImageView>(R.id.searchClearIcon)
		val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)

		val backButton = findViewById<ImageButton>(R.id.searchBackButton)
		backButton.setOnClickListener{
			val displayMain = Intent(this, MainActivity::class.java)
			startActivity(displayMain)
		}

		val inputEditText = findViewById<TextInputEditText>(R.id.searchInputEditText)
		if (savedInstanceState != null) inputEditText.setText(searchString)

		clearSearchButton.setOnClickListener {
			inputEditText.setText(SEARCH_STRING_DEF)
			searchList.clear()
			searchAdapter.notifyDataSetChanged()
			showMessage()
			val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
			inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
		}

		val searchRecyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)
		val searchHistoryRecyclerView = findViewById<RecyclerView>(R.id.searchHistoryRecyclerView)
		val searchHistoryGroup = findViewById<Group>(R.id.searchHistoryGroup)

		inputEditText.setOnFocusChangeListener { view, hasFocus ->
			if (hasFocus) {
				historyAdapter.notifyDataSetChanged()
				searchHistoryGroup.visibility = if (searchHistory.historyList.isEmpty()) View.GONE else View.VISIBLE
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
					searchHistoryGroup.visibility = if (searchHistory.historyList.isEmpty()) View.GONE else View.VISIBLE
					searchRecyclerView.visibility = View.GONE
					showMessage()
				} else {
					searchHistoryGroup.visibility = View.GONE
					searchRecyclerView.visibility = View.VISIBLE
				}
			},
			afterTextChanged = { editable ->
				if (!editable.isNullOrEmpty()) {
					searchString = editable.toString()
					searchDebounce()
				}
			}
		)

		searchRecyclerView.adapter = searchAdapter
		searchHistoryRecyclerView.adapter = historyAdapter

		inputEditText.setOnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_DONE) search()
			false
		}

		updateButton.setOnClickListener {
			search()
		}

		clearHistoryButton.setOnClickListener {
			searchHistory.historyList.clear()
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
		searchList.clear()
		searchAdapter.notifyDataSetChanged()
		showMessage()
		progressBar.visibility = View.VISIBLE
		if (searchString.isNotEmpty())
			itunesApiService.search(searchString).enqueue(object : Callback<SearchResponse> {
					override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse> ) {
						progressBar.visibility = View.GONE
						when (response.code()) {
							200 -> {
								if (response.body()?.searchList?.isNotEmpty() == true) {
									searchList.addAll(response.body()?.searchList!!)
									searchAdapter.notifyDataSetChanged()
									showMessage()
								} else {
									showMessage(getString(R.string.search_nothing_found), R.drawable.ic_nothing_found, View.GONE)
								}
							}
							else -> showMessage(getString(R.string.search_net_error), R.drawable.ic_net_error, View.VISIBLE)
						}
					}

					override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
						progressBar.visibility = View.GONE
						showMessage(getString(R.string.search_net_error), R.drawable.ic_net_error, View.VISIBLE)
					}
			})
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
		searchHistory.addItem(trackItem)
		searchHistory.save()
		val player = Intent(this, PlayerActivity::class.java)
		this.startActivity(player)
	}

	companion object {
		const val SEARCH_STRING_KEY = "SEARCH_STRING"
		const val SEARCH_STRING_DEF = ""
		const val API_URL = "https://itunes.apple.com"
		private const val SEARCH_DEBOUNCE_DELAY = 2000L
	}
}