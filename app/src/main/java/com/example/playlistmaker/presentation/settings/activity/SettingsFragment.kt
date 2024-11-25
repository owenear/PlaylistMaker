package com.example.playlistmaker.presentation.settings.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.nightThemeLiveData.observe(viewLifecycleOwner) { nightTheme ->
            binding.nightThemeSwitch.setChecked(nightTheme)
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