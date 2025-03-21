package com.example.playlistmaker.presentation.player.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.RecyclerItemPlaylistBottomSheetBinding
import com.example.playlistmaker.domain.playlists.models.Playlist

class PlayerPlaylistAdapter(private val clickListener: ((Playlist) -> Unit)? = null):
    RecyclerView.Adapter<PlayerPlaylistViewHolder>() {

    var items: List<Playlist> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemPlaylistBottomSheetBinding.inflate(layoutInflater, parent, false)
        return PlayerPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerPlaylistViewHolder, position: Int) {
        holder.bind(items[position])
        if (clickListener != null)
            holder.itemView.setOnClickListener { clickListener.invoke(items[position]) }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}