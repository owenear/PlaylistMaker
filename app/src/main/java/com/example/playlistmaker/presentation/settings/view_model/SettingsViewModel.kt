package com.example.playlistmaker.presentation.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.api.ThemeInteractor
import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.models.EmailData


class SettingsViewModel(private val themeInteractor: ThemeInteractor,
                        private val sharingInteractor: SharingInteractor,
                        ): ViewModel() {

    private val nightThemeMutableLiveData = MutableLiveData<Boolean>(themeInteractor.getTheme(false))
    val nightThemeLiveData : LiveData<Boolean> = nightThemeMutableLiveData

    fun setTheme(nightTheme: Boolean) {
        nightThemeMutableLiveData.postValue(nightTheme)
        themeInteractor.setTheme(nightTheme)
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

}