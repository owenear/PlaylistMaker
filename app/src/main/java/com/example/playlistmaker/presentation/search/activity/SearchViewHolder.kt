package com.example.playlistmaker.presentation.search.activity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ViewSearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.App

class SearchViewHolder(private val binding: ViewSearchBinding) :
	RecyclerView.ViewHolder(binding.root)
{

	fun bind(item: Track) {

		val coverURL = if (item.artworkUrl100.isNullOrEmpty()) R.drawable.baseline_gesture_24
			else item.artworkUrl100

		Glide.with(itemView)
			.load(coverURL)
			.placeholder(R.drawable.baseline_gesture_24)
			.fitCenter()
			.transform(RoundedCorners((2 * App.DISPLAY_DENSITY).toInt()))
			.into(binding.coverImageView)

		binding.trackTextView.text = item.trackName
		binding.artistTextView.text = item.artistName
		binding.timeTextView.text = item.trackTimeFormat

	}

}