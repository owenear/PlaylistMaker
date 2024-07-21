package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SearchActivity : AppCompatActivity() {
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
		val clearButton = findViewById<ImageView>(R.id.searchClearIcon)

		clearButton.setOnClickListener {
			inputEditText.setText("")
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
				//empty
			}
		}

		inputEditText.addTextChangedListener(searchTextWatcher)
	}

}