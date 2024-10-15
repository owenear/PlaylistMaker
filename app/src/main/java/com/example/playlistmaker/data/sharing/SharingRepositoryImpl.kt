package com.example.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.api.SharingRepository
import com.example.playlistmaker.domain.sharing.models.EmailData

class SharingRepositoryImpl(private val context: Context): SharingRepository {

	override fun openEmail(emailData: EmailData) {
		val emailIntent = Intent(Intent.ACTION_SENDTO)
		emailIntent.data = Uri.parse("mailto:")
		emailIntent.putExtra(Intent.EXTRA_EMAIL, emailData.emailTo)
		//supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_student)))
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
		emailIntent.putExtra(Intent.EXTRA_TEXT, emailData.message)
		context.startActivity(emailIntent)
	}

	override fun openLink(link: String) {
		val opentIntent = Intent(Intent.ACTION_VIEW)
		opentIntent.data = Uri.parse(link)
		context.startActivity(opentIntent)
	}

	override fun shareLink(link: String) {
		val shareIntent = Intent(Intent.ACTION_SEND)
		shareIntent.putExtra(Intent.EXTRA_TEXT, link)
		shareIntent.type = "text/plain"
		context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_prompt)))
	}

}