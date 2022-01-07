package com.svetlana.kuro.notesapp.utils

import android.content.Context
import android.location.Geocoder
import android.os.Handler
import android.os.Looper

class AsyncGeocoder(private val context: Context) {
    private val handler = Handler(Looper.getMainLooper())

    fun getFromLocation(
        latitude: Double,
        longitude: Double,
        maxResults: Int,
        block: (String?) -> Unit
    ) {
        Thread {
            val address = try {
                Geocoder(context).getFromLocation(latitude, longitude, maxResults)
                    .firstOrNull()
            } finally {
                // pass
            }

            handler.post {
                block(address?.getAddressLine(0))
            }
        }.start()
    }

}