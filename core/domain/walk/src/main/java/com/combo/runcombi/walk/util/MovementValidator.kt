package com.combo.runcombi.walk.util

import com.combo.runcombi.walk.model.LocationPoint

object MovementValidator {
    private const val MAX_ACCURACY = 50f
    private const val MIN_DISTANCE = 0.3
    private const val MAX_DISTANCE = 50.0
    private const val MIN_TIME_MS = 1000L
    private const val MAX_TIME_MS = 20000L

    fun isValidMovement(
        prevPoint: LocationPoint,
        newPoint: LocationPoint
    ): Boolean {
        if (newPoint.accuracy > MAX_ACCURACY) return false

        val distance = DistanceCalculator.calculateDistance(prevPoint, newPoint)
        val timeDelta = (newPoint.timestamp - prevPoint.timestamp).coerceAtLeast(1L)

        return distance in MIN_DISTANCE..MAX_DISTANCE &&
                timeDelta in MIN_TIME_MS..MAX_TIME_MS
    }
}

