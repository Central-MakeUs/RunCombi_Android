package com.combo.runcombi.walk.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.walk.model.LocationPoint
import com.combo.runcombi.walk.model.WalkUpdateResult
import com.combo.runcombi.walk.util.DistanceCalculator
import com.combo.runcombi.walk.util.MovementValidator
import javax.inject.Inject

class UpdateWalkRecordUseCase @Inject constructor() {
    operator fun invoke(
        prevPoint: LocationPoint?,
        newPoint: LocationPoint,
        speedList: List<Double>,

    ): DomainResult<WalkUpdateResult> {
        if (prevPoint == null) {
            return DomainResult.Success(
                WalkUpdateResult(
                    distance = 0.0,
                    averageSpeed = 0.0,
                )
            )
        }

        if (!MovementValidator.isValidMovement(prevPoint, newPoint)) {
            return DomainResult.Error()
        }

        val distance = DistanceCalculator.calculateDistance(prevPoint, newPoint)
        val timeDeltaMs = (newPoint.timestamp - prevPoint.timestamp).coerceAtLeast(1000L)
        val speed = distance / (timeDeltaMs / 1000.0)

        val updatedSpeedList = (speedList + speed).takeLast(100)

        val result = WalkUpdateResult(
            distance = distance,
            averageSpeed = updatedSpeedList.average(),
        )
        return DomainResult.Success(result)
    }
}

