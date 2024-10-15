package com.example.playlistmaker.presentation.settings.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.settings.api.ThemeInteractor
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.models.EmailData


class SettingsViewModel(private val themeInteractor: ThemeInteractor,
                        private val sharingInteractor: SharingInteractor,
                        ): ViewModel() {

    private var nightTheme = themeInteractor.getTheme()
    val nightThemeLiveData = MutableLiveData<Boolean>(nightTheme)

    fun setTheme(nightTheme: Boolean) {
        this.nightTheme = nightTheme
        nightThemeLiveData.value = nightTheme
        themeInteractor.setTheme(nightTheme)
        saveTheme()
        Log.d("NIGHTTHEME_SET_THEME", nightTheme.toString())
    }

    private fun saveTheme() {
        themeInteractor.saveTheme(nightTheme)
        Log.d("NIGHTTHEME_SAVE_THEME", nightTheme.toString())
    }

    private fun getTheme() {
        nightTheme = themeInteractor.getTheme()
    }

    fun shareApp(link: String) {
        sharingInteractor.shareApp(link)
    }

    fun openTerms(link: String) {
        sharingInteractor.openTerms(link)
    }

    fun openSupport(emailTo: String, subject: String, message: String) {
        sharingInteractor.openSupport(EmailData(emailTo, subject, message))
    }

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                //val nightTheme = (this[APPLICATION_KEY] as App).nightTheme
                SettingsViewModel(Creator.provideThemeInteractor(context),
                    Creator.provideSharingInteractor(context))
            }
        }
    }

}