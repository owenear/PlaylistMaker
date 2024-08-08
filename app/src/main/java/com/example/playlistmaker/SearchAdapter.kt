package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter (private val items: List<Track>) : RecyclerView.Adapter<SearchViewHolder> (){

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.view_search, parent, false)
		return SearchViewHolder(view)
	}

	override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
		holder.bind(items[position])
	}

	override fun getItemCount(): Int {
		return items.size
	}
}