package com.example.playlistmaker.presentation.player.activity

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.RecyclerItemPlaylistBottomSheetBinding
import com.example.playlistmaker.domain.playlists.models.Playlist
import com.example.playlistmaker.presentation.App

class PlayerPlaylistViewHolder(private val binding: RecyclerItemPlaylistBottomSheetBinding):
RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(item: Playlist) {

        Glide.with(itemView)
            .load(item.coverUri)
            .skipMemoryCache(true)
            .diskCacheStrategy( DiskCacheStrategy.NONE )
            .placeholder(R.drawable.baseline_gesture_24)
            .transform(CenterCrop(), RoundedCorners((8 * App.DISPLAY_DENSITY).toInt()))
            .into(binding.playlistCoverImageView)

        binding.playlistName.text = item.name
        binding.playlistTrackCount.text = item.trackCount.toString() + " " +
                itemView.resources.getQuantityString(
                    R.plurals.track_plurals,
                    item.trackCount, item.trackCount)

    }

}
