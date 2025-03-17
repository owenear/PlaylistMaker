package com.example.playlistmaker.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.data.NetworkConnector

class NetworkBroadcastReceiver(private val networkConnector: NetworkConnector) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION
            && !networkConnector.isConnected()) {
            Toast.makeText(context, context?.getString(R.string.no_network_toast),
                Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}