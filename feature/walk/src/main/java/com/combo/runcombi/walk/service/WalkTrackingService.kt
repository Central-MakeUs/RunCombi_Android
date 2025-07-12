package com.combo.runcombi.walk.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.combo.runcombi.feature.walk.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class WalkTrackingService : Service() {
    companion object {
        // Notification 관련 상수
        const val CHANNEL_ID                = "walk_tracking_channel"
        const val NOTIFICATION_ID           = 1001

        // 위치 브로드캐스트 관련 상수
        const val ACTION_BROADCAST_LOCATION = "com.combo.runcombi.walk.LOCATION_UPDATE"
        const val EXTRA_LATITUDE            = "latitude"
        const val EXTRA_LONGITUDE           = "longitude"
        const val EXTRA_ACCURACY            = "accuracy"
        const val EXTRA_TIME                = "time"
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    val intent = Intent(ACTION_BROADCAST_LOCATION).apply {
                        putExtra(EXTRA_LATITUDE, location.latitude)
                        putExtra(EXTRA_LONGITUDE, location.longitude)
                        putExtra(EXTRA_ACCURACY, location.accuracy)
                        putExtra(EXTRA_TIME, location.time)
                    }
                    sendBroadcast(intent)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        startLocationUpdates()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "운동 기록 서비스",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("RunCombi 운동 기록 중")
            .setContentText("운동 경로와 시간을 기록하고 있습니다.")
            .setSmallIcon(R.drawable.ic_pet_defalut)
            .setOngoing(true)
            .build()
    }

    @Suppress("MissingPermission")
    private fun startLocationUpdates() {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setMinUpdateIntervalMillis(1000)
            .build()
        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
} 