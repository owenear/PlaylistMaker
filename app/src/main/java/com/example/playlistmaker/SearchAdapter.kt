package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class SearchAdapter (private val items: ArrayList<Track>, private val searchHistory: SearchHistory,
                     private val context: Context) : RecyclerView.Adapter<SearchViewHolder> (){

	private val mainHandler = Handler(Looper.getMainLooper())
	private var isClickAllowed = true

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
		return SearchViewHolder(parent)
	}

	override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
		holder.bind(items[position])
		holder.itemView.setOnClickListener {
			if (clickDebounce()) {
				searchHistory.addItem(items[position])
				searchHistory.save()
				val player = Intent(context, PlayerActivity::class.java)
				context.startActivity(player)
			}
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