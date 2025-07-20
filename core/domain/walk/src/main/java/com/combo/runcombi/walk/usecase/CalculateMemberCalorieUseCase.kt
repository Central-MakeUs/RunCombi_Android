package com.combo.runcombi.walk.usecase

import com.combo.runcombi.walk.model.ExerciseType
import javax.inject.Inject

class CalculateMemberCalorieUseCase @Inject constructor() {
    /**
     * @param exerciseType 운동 종류
     * @param gender 성별 (MALE, FEMALE)
     * @param weightKg 체중(kg)
     * @param exerciseHour 운동 시간(h)
     * @param minutes 운동 시간(분)
     * @param seconds 운동 시간(초)
     * @return 칼로리(Double)
     */
    operator fun invoke(
        exerciseType: ExerciseType,
        gender: String,
        weightKg: Double,
        exerciseHour: Double,
    ): Double {
        val met = exerciseType.getMet(gender)
        return met * weightKg * exerciseHour
    }
} 