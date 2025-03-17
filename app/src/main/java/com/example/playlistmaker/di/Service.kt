package com.example.playlistmaker.di

import android.content.BroadcastReceiver
import com.example.playlistmaker.services.NetworkBroadcastReceiver
import org.koin.dsl.module

val serviceModule = module {

    single<BroadcastReceiver> {
        NetworkBroadcastReceiver(get())
    }

}