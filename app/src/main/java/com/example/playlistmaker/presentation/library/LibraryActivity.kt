package com.example.playlistmaker.presentation.library

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding


class LibraryActivity : AppCompatActivity() {

	private lateinit var binding: ActivityLibraryBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		binding = ActivityLibraryBinding.inflate(layoutInflater)
		setContentView(binding.root)

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.library)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		binding.libraryToolbar.setNavigationOnClickListener {
			finish()
		}

		if (savedInstanceState == null) {
			supportFragmentManager.beginTransaction()
				.add(R.id.fragment_container_view, FavouritesFragment())
				.commit()
		}

	}

}