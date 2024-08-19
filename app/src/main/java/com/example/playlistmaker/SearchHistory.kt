package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {

	var historyList = ArrayList<Track>(10)

	init {
		val arrayListType = object : TypeToken<ArrayList<Track>>() {}.type
		historyList = Gson().fromJson(sharedPreferences.getString(SEARCH_HISTORY_KEY, SEARCH_HISTORY_DEF), arrayListType)
	}

	fun addItem(item: Track) {
		if (item in historyList) historyList.remove(item)
		historyList.add(0, item)
		if (historyList.size > 10) historyList.removeAt(10)
	}

	fun save() {
		sharedPreferences.edit()
			.putString(SEARCH_HISTORY_KEY, Gson().toJson(historyList))
			.apply()
	}

	companion object {
		const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
		const val SEARCH_HISTORY_DEF = "[]"
	}
}