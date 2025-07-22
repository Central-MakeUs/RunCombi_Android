package com.combo.runcombi.walk.util


import java.util.Locale

object FormatUtils {
    fun formatTime(time: Int): String {
        val hours = time / 3600
        val minutes = (time % 3600) / 60
        val seconds = time % 60
        return if (hours > 0) {
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }

    fun formatMinute(time: Int): String {
        val minutes = time / 60
        return minutes.toString()
    }

    fun formatDistance(distance: Double): String {
        val distanceKm = distance / 1000.0
        return String.format(Locale.getDefault(), "%.2f", distanceKm)
    }

    fun formatCalorie(calorie: Double): String {
        return calorie.toInt().toString()
    }
}