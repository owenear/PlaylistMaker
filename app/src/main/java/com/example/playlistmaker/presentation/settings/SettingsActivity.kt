package com.example.playlistmaker.presentation.settings

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var themeSwitcher: SwitchCompat

    override fun onStop() {
    //    settingsViewModel.saveTheme((applicationContext as App).nightTheme)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        settingsViewModel = ViewModelProvider(this,
            SettingsViewModel.getViewModelFactory(applicationContext))[SettingsViewModel::class.java]

        themeSwitcher = findViewById<SwitchCompat>(R.id.nightThemeSwitch)

        settingsViewModel.nightThemeLiveData.observe(this) { nightTheme ->
            Log.d("NIGHTTHEME", nightTheme.toString())
            themeSwitcher.setChecked(nightTheme)
        }

        //themeSwitcher.setChecked((applicationContext as App).nightTheme)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButtonToolbar = findViewById<MaterialToolbar>(R.id.settingsToolbar)
        backButtonToolbar.setOnClickListener{
            finish()
        }

        val shareButton = findViewById<FrameLayout>(R.id.shareFrame)
        shareButton.setOnClickListener{
            settingsViewModel.shareApp(getString(R.string.practicum_android_link))
        }

        val supportButton = findViewById<FrameLayout>(R.id.supportFrame)
        supportButton.setOnClickListener{
            settingsViewModel.openSupport(getString(R.string.email_student),
                                        getString(R.string.email_subject),
                                        getString(R.string.email_message))
        }

        val agreementButton = findViewById<FrameLayout>(R.id.usrAgrFrame)
        agreementButton.setOnClickListener{
            settingsViewModel.openTerms(getString(R.string.practicum_offer_link))
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            if(switcher.isPressed) settingsViewModel.setTheme(checked)
        }

    }

}