package com.combo.runcombi.walk.usecase

import javax.inject.Inject

class CalculatePetCalorieUseCase @Inject constructor() {
    /**
     * @param weightKg 몸무게(kg)
     * @param exerciseHour 운동 시간(h)
     * @param activityFactor 활동 계수
     * @return 소모 칼로리(Double)
     */
    operator fun invoke(weightKg: Double, exerciseHour: Double, activityFactor: Double): Double {
        val rer = 70 * Math.pow(weightKg, 0.75)
        return rer * activityFactor * exerciseHour
    }
}



