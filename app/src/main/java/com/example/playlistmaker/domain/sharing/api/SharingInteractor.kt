package com.example.playlistmaker.domain.sharing.api

import com.example.playlistmaker.domain.sharing.models.EmailData

interface SharingInteractor {
	fun shareApp(link: String)
	fun openTerms(link: String)
	fun openSupport(emailData: EmailData)
}