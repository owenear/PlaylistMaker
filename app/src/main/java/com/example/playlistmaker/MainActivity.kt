package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchButton = findViewById<Button>(R.id.searchButton)
        val libraryButton = findViewById<Button>(R.id.libraryButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)

        searchButton.setOnClickListener{
            val displaySearch = Intent(this, SearchActivity::class.java)
            startActivity(displaySearch)
        }

        libraryButton.setOnClickListener{
            val displayLibrary = Intent(this, LibraryActivity::class.java)
            startActivity(displayLibrary)
        }


        val settingsButtonClickListener: View.OnClickListener = View.OnClickListener {
            val displaySettings = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(displaySettings)
        }
        settingsButton.setOnClickListener(settingsButtonClickListener)
    }
}