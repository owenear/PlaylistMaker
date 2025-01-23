package com.example.playlistmaker.di
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.SharedStorage
import com.example.playlistmaker.data.AppDatabase
import com.example.playlistmaker.util.mappers.TrackHistoryMapper
import com.example.playlistmaker.util.mappers.TrackMapper
import com.example.playlistmaker.data.search.network.ItunesApi
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.storage.TrackHistorySharedStorage
import com.example.playlistmaker.data.settings.storage.ThemeSharedStorage
import com.example.playlistmaker.util.mappers.PlaylistMapper
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_URL = "https://itunes.apple.com"
const val SHARED_PREFERENCES_FILE = "playlist_maker_preferences"

val dataModule = module {

    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)
    }

    single<SharedStorage>(named("theme")) {
        ThemeSharedStorage(get())
    }

    single<SharedStorage>(named("track_history")) {
        TrackHistorySharedStorage(get(), get())
    }

    factory {
        Gson()
    }

    single{
        TrackMapper()
    }

    single{
        TrackHistoryMapper()
    }

    single{
        PlaylistMapper()
    }

    factory {
        MediaPlayer()
    }

}