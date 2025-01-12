package com.example.playlistmaker.data

interface SharedStorage {

	fun putData(data: Any)

	fun getData(defaultData: Any? = null): Any

}