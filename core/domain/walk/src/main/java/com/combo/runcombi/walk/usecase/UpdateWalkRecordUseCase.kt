package com.combo.runcombi.walk.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.walk.model.LocationPoint
import com.combo.runcombi.walk.model.WalkUpdateResult
import com.combo.runcombi.walk.util.CalorieCalculator
import com.combo.runcombi.walk.util.DistanceCalculator
import com.combo.runcombi.walk.util.MovementValidator
import javax.inject.Inject

class UpdateWalkRecordUseCase @Inject constructor() {

    private val speedList = mutableListOf<Double>()

    fun execute(
        prevPoint: LocationPoint?,
        newPoint: LocationPoint,
    ): DomainResult<WalkUpdateResult> {
        if (prevPoint == null) {
            return DomainResult.Success(
                WalkUpdateResult(
                    distance = 0.0,
                    averageSpeed = 0.0,
                    calorie = 0.0
                )
            )
        }

        if (!MovementValidator.isValidMovement(prevPoint, newPoint)) {
            return DomainResult.Error()
        }

        val distance = DistanceCalculator.calculateDistance(prevPoint, newPoint)
        val timeDeltaMs = (newPoint.timestamp - prevPoint.timestamp).coerceAtLeast(1000L)
        val speed = distance / (timeDeltaMs / 1000.0)

        if (speedList.size >= 100) {
            speedList.removeAt(0)
        }
        speedList += speed

        val result = WalkUpdateResult(
            distance = distance,
            averageSpeed = speedList.average(),
            calorie = CalorieCalculator.calculateCalories(distance)
        )
        return DomainResult.Success(result)
    }


    fun reset() {
        speedList.clear()
    }
}

