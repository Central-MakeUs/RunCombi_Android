package com.combo.runcombi.walk.usecase

import javax.inject.Inject

class CalculatePetCalorieUseCase @Inject constructor() {
    /**
     * @param weightKg 몸무게(kg)
     * @param distance 거리(km)
     * @param activityFactor 활동 계수
     * @return 소모 칼로리(Double)
     */
    operator fun invoke(weightKg: Double, distance: Double, activityFactor: Double): Double {
        return weightKg * activityFactor * distance
    }
}



