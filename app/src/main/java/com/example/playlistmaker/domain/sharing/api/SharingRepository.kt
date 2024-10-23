package com.example.playlistmaker.domain.sharing.api

import com.example.playlistmaker.domain.sharing.models.EmailData

interface SharingRepository {
	fun shareLink(link: String)
	fun openLink(link: String)
	fun openEmail(emailData: EmailData)
}