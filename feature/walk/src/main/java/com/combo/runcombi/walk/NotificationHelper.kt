package com.combo.runcombi.walk

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.combo.runcombi.feature.walk.R

class NotificationHelper(private val context: Context) {
    companion object {
        const val CHANNEL_ID = "walk_tracking_channel"
        const val NOTIFICATION_ID = 1001
    }

    fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "운동 기록 서비스",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun createNotification(): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("RunCombi")
            .setContentText("운동 경로와 시간을 기록하고 있습니다.")
            .setSmallIcon(R.drawable.ic_pet_defalut)
            .setOngoing(true)
            .build()
    }

    fun cancelNotification() {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID)
    }
} 