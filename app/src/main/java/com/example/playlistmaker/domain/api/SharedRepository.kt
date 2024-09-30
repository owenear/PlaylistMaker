package com.example.playlistmaker.domain.api

interface SharedRepository {
	fun putData(data: Any)
	fun getData() : Any
}