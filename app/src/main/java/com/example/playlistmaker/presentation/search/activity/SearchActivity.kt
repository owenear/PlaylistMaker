package com.example.playlistmaker.presentation.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.activity.PlayerActivity
import com.example.playlistmaker.presentation.search.models.SearchScreenState
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText


class SearchActivity : AppCompatActivity() {

	private var searchString: String = SEARCH_STRING_DEF

	private var searchList = mutableListOf<Track>()
	private var historyList = mutableListOf<Track>()

	private val searchAdapter by lazy {
		SearchAdapter(searchList) { trackItem -> trackListClickListener(trackItem) }
	}
	private val historyAdapter by lazy {
		SearchAdapter(historyList) { trackItem -> trackListClickListener(trackItem) }
	}

	private val searchViewModel by lazy {
		ViewModelProvider(
			this,
			SearchViewModel.getViewModelFactory(applicationContext)
		)[SearchViewModel::class.java]
	}

	private lateinit var placeholderMessage: TextView
	private lateinit var placeholderImage: ImageView
	private lateinit var updateButton: Button
	private lateinit var searchHistoryGroup: Group
	private lateinit var searchRecyclerView: RecyclerView
	private lateinit var progressBar: ProgressBar
	private lateinit var inputEditText: TextInputEditText

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putString(SEARCH_STRING_KEY, searchString)
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		searchString = savedInstanceState.getString(SEARCH_STRING_KEY, SEARCH_STRING_DEF)
	}

	override fun onResume() {
		super.onResume()
		if (searchString.isEmpty() && inputEditText.hasFocus())
			searchViewModel.historyContent()
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

		placeholderMessage = findViewById<TextView>(R.id.placeholderTextView)
		placeholderImage = findViewById<ImageView>(R.id.placeholderImageView)
		updateButton = findViewById<Button>(R.id.updateButton)
		progressBar = findViewById<ProgressBar>(R.id.progressBar)
		searchHistoryGroup = findViewById<Group>(R.id.searchHistoryGroup)

		val clearSearchButton = findViewById<ImageView>(R.id.searchClearIcon)
		val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
		val backButtonToolbar = findViewById<MaterialToolbar>(R.id.searchToolbar)
		inputEditText = findViewById<TextInputEditText>(R.id.searchInputEditText)

		searchRecyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)
		searchRecyclerView.adapter = searchAdapter

		val searchHistoryRecyclerView = findViewById<RecyclerView>(R.id.searchHistoryRecyclerView)
		searchHistoryRecyclerView.adapter = historyAdapter

		if (savedInstanceState != null) inputEditText.setText(searchString)

		backButtonToolbar.setNavigationOnClickListener {
			finish()
		}

		clearSearchButton.setOnClickListener {
			inputEditText.setText(SEARCH_STRING_DEF)
			searchViewModel.historyContent()
			val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
			inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
		}

		inputEditText.setOnFocusChangeListener { view, hasFocus ->
			if (hasFocus) {
				searchViewModel.historyContent()
			} else {
				render(SearchScreenState.Initial)
			}
		}

		inputEditText.addTextChangedListener(
			onTextChanged = { charSequence, _, _, _ ->
				clearSearchButton.visibility = if (charSequence.isNullOrEmpty()) View.GONE else View.VISIBLE
				if (inputEditText.hasFocus() && charSequence.isNullOrEmpty()) {
					searchViewModel.historyContent()
				} else {
					render(SearchScreenState.Initial)
				}
			},
			afterTextChanged = { editable ->
				if (!editable.isNullOrEmpty()) {
					searchString = editable.toString()
					searchViewModel.searchDebounce(searchString)
				}
			}
		)

		inputEditText.setOnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_DONE && !inputEditText.text.isNullOrEmpty()) {
				searchString = inputEditText.text.toString()
				searchViewModel.search(searchString)
			}
			false
		}

		updateButton.setOnClickListener {
			searchViewModel.search(searchString)
		}

		clearHistoryButton.setOnClickListener {
			searchViewModel.historyClear()
		}

		searchViewModel.stateLiveData.observe(this) { state ->
			render(state)
		}

	}

	private fun trackListClickListener(trackItem: Track) {
		searchViewModel.historyAddTrack(trackItem)
		val playerIntent = Intent(this, PlayerActivity::class.java)
		playerIntent.putExtra("trackItem",trackItem)
		this.startActivity(playerIntent)
	}

	private fun render(state: SearchScreenState) {
		when (state) {
			is SearchScreenState.Initial -> showInitial()
			is SearchScreenState.Loading -> showLoading()
			is SearchScreenState.SearchContent -> showSearchContent(state.trackList)
			is SearchScreenState.HistoryContent -> showHistoryContent(state.trackList)
			is SearchScreenState.Error -> showError()
			is SearchScreenState.Empty -> showEmpty()
		}
	}


	private fun showInitial(){
		placeholderMessage.visibility = View.GONE
		placeholderImage.visibility = View.GONE
		updateButton.visibility = View.GONE
		progressBar.visibility = View.GONE
		searchHistoryGroup.visibility = View.GONE
		searchRecyclerView.visibility = View.GONE
	}

	private fun showLoading() {
		searchHistoryGroup.visibility = View.GONE
		searchRecyclerView.visibility = View.GONE
		placeholderMessage.visibility = View.GONE
		placeholderImage.visibility = View.GONE
		updateButton.visibility = View.GONE
		progressBar.visibility = View.VISIBLE
	}

	private fun showError() {
		searchHistoryGroup.visibility = View.GONE
		searchRecyclerView.visibility = View.GONE
		progressBar.visibility = View.GONE
		placeholderMessage.visibility = View.VISIBLE
		placeholderImage.visibility = View.VISIBLE
		updateButton.visibility = View.VISIBLE
		placeholderMessage.text = getString(R.string.search_net_error)
		placeholderImage.setImageResource(R.drawable.ic_net_error)
	}

	private fun showEmpty() {
		searchHistoryGroup.visibility = View.GONE
		searchRecyclerView.visibility = View.GONE
		progressBar.visibility = View.GONE
		placeholderMessage.visibility = View.VISIBLE
		placeholderImage.visibility = View.VISIBLE
		updateButton.visibility = View.GONE
		placeholderMessage.text = getString(R.string.search_nothing_found)
		placeholderImage.setImageResource(R.drawable.ic_nothing_found)
	}

	private fun showSearchContent(trackList: List<Track>){
		searchList.clear()
		searchList.addAll(trackList)
		searchAdapter.notifyDataSetChanged()
		placeholderMessage.visibility = View.GONE
		placeholderImage.visibility = View.GONE
		updateButton.visibility = View.GONE
		progressBar.visibility = View.GONE
		searchHistoryGroup.visibility = View.GONE
		searchRecyclerView.visibility = View.VISIBLE
	}

	private fun showHistoryContent(trackList: List<Track>){
		historyList.clear()
		historyList.addAll(trackList)
		historyAdapter.notifyDataSetChanged()
		placeholderMessage.visibility = View.GONE
		placeholderImage.visibility = View.GONE
		updateButton.visibility = View.GONE
		progressBar.visibility = View.GONE
		searchRecyclerView.visibility = View.GONE
		searchHistoryGroup.visibility = if (trackList.isEmpty()) View.GONE else View.VISIBLE
	}

	companion object {
		const val SEARCH_STRING_KEY = "SEARCH_STRING"
		const val SEARCH_STRING_DEF = ""
	}
}