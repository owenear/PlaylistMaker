package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.api.SharingInteractor
import com.example.playlistmaker.domain.sharing.api.SharingRepository
import com.example.playlistmaker.domain.sharing.models.EmailData

class SharingInteractorImpl(private val sharingRepository: SharingRepository) : SharingInteractor {

	override fun shareApp(link: String) {
		sharingRepository.shareLink(link)
	}

	override fun openTerms(link: String) {
		sharingRepository.openLink(link)
	}

	override fun openSupport(emailData: EmailData) {
		sharingRepository.openEmail(emailData)
	}

	/*
	private fun getShareAppLink(): String {
		// Нужно реализовать
	}

	private fun getSupportEmailData(): EmailData {
		// Нужно реализовать
	}

	private fun getTermsLink(): String {
		// Нужно реализовать
	}
*/
}