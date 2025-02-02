package com.example.playlistmaker.presentation.search.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.RecyclerItemSearchBinding
import com.example.playlistmaker.domain.search.models.Track


class SearchAdapter (private val longClickListener: ((Track) -> Boolean)? = null,
						private val clickListener: (Track) -> Unit)
	: RecyclerView.Adapter<SearchViewHolder> (){

	var items: List<Track> = emptyList()
		@SuppressLint("NotifyDataSetChanged")
        set(newValue) {
			field = newValue
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		val binding = RecyclerItemSearchBinding.inflate(layoutInflater, parent, false)
		return SearchViewHolder(binding)
	}

	override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
		holder.bind(items[position])
		holder.itemView.setOnClickListener { clickListener.invoke(items[position]) }
		if (longClickListener != null) {
			holder.itemView.setOnLongClickListener {
				longClickListener.invoke(items[position])
			}
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

}