package com.example.playlistmaker.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class SearchViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
	LayoutInflater.from(parent.context).inflate(R.layout.view_search, parent, false)
)
{
	private val trackNameTV: TextView = itemView.findViewById(R.id.trackTextView)
	private val artistNameTV: TextView = itemView.findViewById(R.id.artistTextView)
	private val trackTimeTV: TextView = itemView.findViewById(R.id.timeTextView)
	private val coverIV: ImageView = itemView.findViewById(R.id.coverImageView)

	fun bind(item: Track) {

		val coverURL = if (item.artworkUrl100.isNullOrEmpty()) R.drawable.baseline_gesture_24
			else item.artworkUrl100

		Glide.with(itemView)
			.load(coverURL)
			.placeholder(R.drawable.baseline_gesture_24)
			.fitCenter()
			.transform(RoundedCorners((2 * App.DISPLAY_DENSITY).toInt()))
			.into(coverIV)

		trackNameTV.text = item.trackName
		artistNameTV.text = item.artistName
		trackTimeTV.text = item.trackTimeFormat

	}

}