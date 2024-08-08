package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class SearchActivity : AppCompatActivity() {

	private var searchString: String = SEARCH_STRING_DEF

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putString(SEARCH_STRING_KEY, searchString)
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		searchString = savedInstanceState.getString(SEARCH_STRING_KEY, SEARCH_STRING_DEF)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_search)

		val backButton = findViewById<ImageButton>(R.id.searchBackButton)
		backButton.setOnClickListener{
			val displayMain = Intent(this, MainActivity::class.java)
			startActivity(displayMain)
		}

		val inputEditText = findViewById<TextInputEditText>(R.id.searchInputEditText)
		if (savedInstanceState != null) inputEditText.setText(searchString)

		val clearButton = findViewById<ImageView>(R.id.searchClearIcon)

		clearButton.setOnClickListener {
			inputEditText.setText(SEARCH_STRING_DEF)
			val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
			inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
		}

		val searchTextWatcher = object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
				//empty
			}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				if (s.isNullOrEmpty()) clearButton.visibility = View.GONE
				else clearButton.visibility = View.VISIBLE
			}

			override fun afterTextChanged(s: Editable?) {
				if (s.isNullOrEmpty()) searchString = s.toString()
			}
		}
		inputEditText.addTextChangedListener(searchTextWatcher)


		val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

		val searchList =
			listOf(
				Track(getString(R.string.search_track1_name), getString(R.string.search_track1_artist), getString(R.string.search_track1_time), getString(R.string.search_track1_cover)),
				Track(getString(R.string.search_track2_name), getString(R.string.search_track2_artist), getString(R.string.search_track2_time), getString(R.string.search_track2_cover)),
				Track(getString(R.string.search_track3_name), getString(R.string.search_track3_artist), getString(R.string.search_track3_time), getString(R.string.search_track3_cover)),
				Track(getString(R.string.search_track4_name), getString(R.string.search_track4_artist), getString(R.string.search_track4_time), getString(R.string.search_track4_cover)),
				Track(getString(R.string.search_track5_name), getString(R.string.search_track5_artist), getString(R.string.search_track5_time), getString(R.string.search_track5_cover)),
			)
		recyclerView.adapter = SearchAdapter(searchList)
	}

	companion object {
		const val SEARCH_STRING_KEY = "SEARCH_STRING"
		const val SEARCH_STRING_DEF = ""
	}
}