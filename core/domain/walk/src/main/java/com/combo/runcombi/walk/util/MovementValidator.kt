package com.combo.runcombi.walk.util

import com.combo.runcombi.walk.model.LocationPoint

object MovementValidator {
    fun isValidMovement(
        prevPoint: LocationPoint?,
        newPoint: LocationPoint
    ): Boolean {
        if (newPoint.accuracy > 30f) return false
        if (prevPoint == null) return true

        val distance = DistanceCalculator.calculateDistance(prevPoint, newPoint)
        val timeDelta = (newPoint.timestamp - prevPoint.timestamp).coerceAtLeast(1L)

        return distance in 0.5..50.0 &&
                timeDelta in 1000..10000
    }
}
