package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter (private val items: ArrayList<Track>) : RecyclerView.Adapter<SearchViewHolder> (){

	private var searchHistory: SearchHistory? = null

	constructor(items: ArrayList<Track>, searchHistory: SearchHistory) : this(items){
		this.searchHistory = searchHistory
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
		return SearchViewHolder(parent)
	}

	override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
		holder.bind(items[position])
		holder.itemView.setOnClickListener {
			searchHistory?.addItem(items[position])
			searchHistory?.save()
		}
	}

	override fun getItemCount(): Int {
		return items.size
	}

}