package com.example.playlistmaker.presentation.library.activity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.RecyclerItemPlaylistBinding
import com.example.playlistmaker.domain.playlist.models.Playlist
import com.example.playlistmaker.presentation.App

class PlaylistViewHolder(private val binding: RecyclerItemPlaylistBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist) {

        val coverURI = if (item.coverUri == null) R.drawable.baseline_gesture_24
        else item.coverUri

        Glide.with(itemView)
            .load(coverURI)
            .placeholder(R.drawable.baseline_gesture_24)
            .fitCenter()
            .transform(RoundedCorners((8 * App.DISPLAY_DENSITY).toInt()))
            .into(binding.playlistCoverImageView)

        binding.playlistName.text = item.name
    }
}