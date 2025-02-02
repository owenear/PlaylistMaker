package com.example.playlistmaker.presentation.playlists.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.RecyclerItemPlaylistLibraryBinding
import com.example.playlistmaker.domain.playlists.models.Playlist


class PlaylistAdapter(private val clickListener: ((Playlist) -> Unit)? = null):
    RecyclerView.Adapter<PlaylistViewHolder>() {

    var items: List<Playlist> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemPlaylistLibraryBinding.inflate(layoutInflater, parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(items[position])
        if (clickListener != null)
            holder.itemView.setOnClickListener { clickListener.invoke(items[position]) }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}