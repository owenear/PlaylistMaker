package com.example.playlistmaker.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.models.NightTheme
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeInteractor : ThemeInteractor

    override fun onStop() {
        themeInteractor.saveTheme(NightTheme)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        themeInteractor = Creator.provideThemeInteractor(applicationContext)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.nightThemeSwitch)
        themeSwitcher.setChecked(NightTheme.nightTheme)

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
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_android_link))
            shareIntent.type = "text/plain"
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_prompt)))
        }

        val supportButton = findViewById<FrameLayout>(R.id.supportFrame)
        supportButton.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_student)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
            startActivity(supportIntent)
        }

        val agreementButton = findViewById<FrameLayout>(R.id.usrAgrFrame)
        agreementButton.setOnClickListener{
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(getString(R.string.practicum_offer_link))
            startActivity(agreementIntent)
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            if(switcher.isPressed) (applicationContext as App).switchTheme(checked)
        }

    }

}