package com.example.playlistmaker.presentation.settings.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsViewModel.nightThemeLiveData.observe(this) { nightTheme ->
            binding.nightThemeSwitch.setChecked(nightTheme)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.settingsToolbar.setOnClickListener{
            finish()
        }

        binding.shareTextView.setOnClickListener{
            settingsViewModel.shareApp(getString(R.string.practicum_android_link))
        }

        binding.supportTextView.setOnClickListener{
            settingsViewModel.openSupport(getString(R.string.email_student),
                                        getString(R.string.email_subject),
                                        getString(R.string.email_message))
        }

        binding.agreementTextView.setOnClickListener{
            settingsViewModel.openTerms(getString(R.string.practicum_offer_link))
        }

        binding.nightThemeSwitch.setOnCheckedChangeListener { switcher, checked ->
            if(switcher.isPressed) settingsViewModel.setTheme(checked)
        }

    }

}