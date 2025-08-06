package com.combo.runcombi.walk.model

enum class ExerciseType {
    RUNNING,
    WALKING,
    SLOW_WALKING;

    fun getActivityFactor(gender: String): Double = when (this) {
        RUNNING -> if (gender == "MALE") 1.09 else 0.70
        WALKING -> if (gender == "MALE") 0.88 else 0.77
        SLOW_WALKING -> if (gender == "MALE") 0.82 else 0.70
    }
}