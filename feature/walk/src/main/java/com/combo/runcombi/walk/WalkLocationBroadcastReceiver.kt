package com.combo.runcombi.walk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WalkLocationBroadcastReceiver(
    private val isPausedProvider: () -> Boolean,
    private val onLocationReceived: (lat: Double, lng: Double, acc: Float, time: Long) -> Unit
) : BroadcastReceiver() {
    companion object {
        const val ACTION_BROADCAST_LOCATION = "com.combo.runcombi.walk.LOCATION_UPDATE"
        const val EXTRA_LATITUDE = "latitude"
        const val EXTRA_LONGITUDE = "longitude"
        const val EXTRA_ACCURACY = "accuracy"
        const val EXTRA_TIME = "time"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_BROADCAST_LOCATION) {
            val lat = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0)
            val lng = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0)
            val acc = intent.getFloatExtra(EXTRA_ACCURACY, 0f)
            val time = intent.getLongExtra(EXTRA_TIME, 0L)
            if (!isPausedProvider()) {
                onLocationReceived(lat, lng, acc, time)
            }
        }
    }
} 