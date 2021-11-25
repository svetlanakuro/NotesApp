package com.svetlana.kuro.notesapp.ui.main

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager

const val NETWORK_STATUS_INTENT_FILTER = "NetworkStatusIntentFilter"
const val NETWORK_STATUS = "NetworkStatus"
const val AVAILABLE_STATUS = "AvailableStatus"
const val LOST_STATUS = "LostStatus"

class NetworkMonitor(private val application: Application) {
    private val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            doCallback(AVAILABLE_STATUS)
        }

        override fun onLost(network: Network) {
            doCallback(LOST_STATUS)
        }
    }

    private fun doCallback(status: String) {
        LocalBroadcastManager.getInstance(application)
            .sendBroadcast(
                Intent(NETWORK_STATUS_INTENT_FILTER)
                    .putExtra(NETWORK_STATUS, status)
            )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startNetworkCallback() {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun stopNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}