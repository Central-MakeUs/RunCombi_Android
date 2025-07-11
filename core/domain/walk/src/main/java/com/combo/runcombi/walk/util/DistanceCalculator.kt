package com.combo.runcombi.walk.util

import com.combo.runcombi.walk.model.LocationPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object DistanceCalculator {
    fun calculateDistance(start: LocationPoint, end: LocationPoint): Double {
        val radius = 6371000.0 // Earth radius in meters
        val dLat = Math.toRadians(end.latitude - start.latitude)
        val dLng = Math.toRadians(end.longitude - start.longitude)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(start.latitude)) *
                cos(Math.toRadians(end.latitude)) *
                sin(dLng / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }
}
