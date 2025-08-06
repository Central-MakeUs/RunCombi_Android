package com.combo.runcombi.walk.usecase

import com.combo.runcombi.walk.model.ExerciseType
import javax.inject.Inject

class CalculateMemberCalorieUseCase @Inject constructor() {
    /**
     * @param exerciseType 운동 종류
     * @param gender 성별 (MALE, FEMALE)
     * @param weightKg 체중(kg)
     * @param distance 거리(km)
     * @return 칼로리(Double)
     */
    operator fun invoke(
        exerciseType: ExerciseType,
        gender: String,
        weightKg: Double,
        distance: Double
    ): Double {
        val activityFactor = exerciseType.getActivityFactor(gender)
        return activityFactor * weightKg * distance
    }
} 