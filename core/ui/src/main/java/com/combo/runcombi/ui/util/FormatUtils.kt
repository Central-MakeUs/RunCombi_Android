package com.combo.runcombi.ui.util


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

    fun formatDistance(distance: Double, decimalPlaces: Int = 2): String {
        val formatString = "%.${decimalPlaces}f"
        return String.format(Locale.getDefault(), formatString, distance)
    }

    fun formatCalorie(calorie: Double): String {
        return calorie.toInt().toString()
    }

    fun formatDateKorean(dateTimeString: String): String {
        return try {
            val dateTime = java.time.LocalDateTime.parse(dateTimeString)
            "${dateTime.year}년 ${dateTime.monthValue}월 ${dateTime.dayOfMonth}일"
        } catch (e: Exception) {
            dateTimeString 
        }
    }

    fun formatDate(dateString: String): String {
        return try {
            if (dateString.contains("-")) {
                dateString.replace("-", ".")
            } else {
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }
}