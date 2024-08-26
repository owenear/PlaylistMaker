package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class SearchAdapter (private val items: ArrayList<Track>, private val searchHistory: SearchHistory,
                     private val context: Context) : RecyclerView.Adapter<SearchViewHolder> (){

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
		return SearchViewHolder(parent)
	}

	override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
		holder.bind(items[position])
		holder.itemView.setOnClickListener {
			searchHistory.addItem(items[position])
			searchHistory.save()
			val player = Intent(context, PlayerActivity::class.java)
			context.startActivity(player)
		}

	}

	override fun getItemCount(): Int {
		return items.size
	}

}