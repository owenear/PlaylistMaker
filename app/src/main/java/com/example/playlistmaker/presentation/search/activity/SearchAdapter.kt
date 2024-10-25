package com.example.playlistmaker.presentation.search.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.search.models.Track


class SearchAdapter (private val clickListener: (Track) -> Unit)
	: RecyclerView.Adapter<SearchViewHolder> (){

	var items: List<Track> = emptyList()
		@SuppressLint("NotifyDataSetChanged")
        set(newValue) {
			field = newValue
			notifyDataSetChanged()
		}

	private val mainHandler = Handler(Looper.getMainLooper())
	private var isClickAllowed = true

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
		return SearchViewHolder(parent)
	}

	override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
		holder.bind(items[position])
		holder.itemView.setOnClickListener {
			if (clickDebounce()) clickListener.invoke(items[position])
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

	private fun clickDebounce() : Boolean {
		val current = isClickAllowed
		if (isClickAllowed) {
			isClickAllowed = false
			mainHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
		}
		return current
	}

	companion object {
		private const val CLICK_DEBOUNCE_DELAY = 1000L
	}
}