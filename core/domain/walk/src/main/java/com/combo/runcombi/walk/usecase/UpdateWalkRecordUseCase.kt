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

    suspend fun execute(
        prevPoint: LocationPoint?,
        newPoint: LocationPoint,
    ): DomainResult<WalkUpdateResult> {
        return if (!MovementValidator.isValidMovement(prevPoint, newPoint)) {
            DomainResult.Error()
        } else {
            val distance = DistanceCalculator.calculateDistance(prevPoint!!, newPoint)
            val timeDeltaSec = (newPoint.timestamp - prevPoint.timestamp).coerceAtLeast(1L) / 1000.0
            val speed = distance / timeDeltaSec

            speedList += speed

            val result = WalkUpdateResult(
                distance = distance,
                averageSpeed = speedList.average(),
                calorie = CalorieCalculator.calculateCalories(distance)
            )
            DomainResult.Success(result)
        }
    }

    fun reset() {
        speedList.clear()
    }
}
