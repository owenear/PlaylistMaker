package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
	private val trackNameTV: TextView = itemView.findViewById(R.id.trackTextView)
	private val artistNameTV: TextView = itemView.findViewById(R.id.artistTextView)
	private val trackTimeTV: TextView = itemView.findViewById(R.id.timeTextView)
	private val coverIV: ImageView = itemView.findViewById(R.id.coverImageView)


	fun bind(item: Track) {

		Glide.with(itemView)
			.load(item.artworkUrl100)
			.placeholder(R.drawable.baseline_accessible_forward_44)
			.fitCenter()
			.transform(RoundedCorners(2))
			.into(coverIV)

		trackNameTV.text = item.trackName
		artistNameTV.text = item.artistName
		trackTimeTV.text = item.trackTime

	}

}