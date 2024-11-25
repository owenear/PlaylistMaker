package com.example.playlistmaker.presentation.library.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator


class LibraryActivity : AppCompatActivity() {

	private lateinit var binding: ActivityLibraryBinding

	private lateinit var tabMediator: TabLayoutMediator

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

		binding.libraryViewPager.adapter = LibraryPagerAdapter(supportFragmentManager, lifecycle)

		tabMediator = TabLayoutMediator(binding.libraryTabLayout, binding.libraryViewPager) { tab, position ->
			when(position) {
				0 -> tab.text = getString(R.string.library_tab1_name)
				1 -> tab.text = getString(R.string.library_tab2_name)
			}
		}
		tabMediator.attach()

	}

	override fun onDestroy() {
		super.onDestroy()
		tabMediator.detach()
	}

}