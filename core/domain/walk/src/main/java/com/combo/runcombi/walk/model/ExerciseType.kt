package com.combo.runcombi.walk.model

enum class ExerciseType {
    RUNNING,
    WALKING,
    SLOW_WALKING;

    fun getActivityFactor(gender: String): Double = when (this) {
        RUNNING -> if (gender == "MALE") 1.09 else 1.01
        WALKING -> if (gender == "MALE") 0.88 else 0.77
        SLOW_WALKING -> if (gender == "MALE") 0.82 else 0.70
    }

    companion object {
        fun fromOrDefault(
            name: String,
            default: ExerciseType = ExerciseType.RUNNING,
        ): ExerciseType {
            return try {
                valueOf(name)
            } catch (e: IllegalArgumentException) {
                default
            }
        }
    }
}