package com.example.playlistmaker.presentation.playlists.activity

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.RecyclerItemPlaylistLibraryBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.presentation.App

class PlaylistViewHolder(private val binding: RecyclerItemPlaylistLibraryBinding):
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(item: Playlist) {

        Glide.with(itemView)
            .load(item.coverUri)
            .placeholder(R.drawable.baseline_gesture_24)
            .transform(CenterCrop(),RoundedCorners((8 * App.DISPLAY_DENSITY).toInt()))
            .into(binding.playlistCoverImageView)

        binding.playlistName.text = item.name
        binding.playlistTrackCount.text = item.trackCount.toString() + " " +
                itemView.resources.getQuantityString(R.plurals.track_plurals,
                    item.trackCount, item.trackCount)
    }

}