package com.combo.runcombi.walk.model

enum class ExerciseType {
    RUNNING,
    WALKING,
    SLOW_WALKING;

    fun getMet(gender: String): Double = when (this) {
        RUNNING -> if (gender == "MALE") 7.5 else 7.0
        WALKING -> if (gender == "MALE") 4.8 else 4.2
        SLOW_WALKING -> if (gender == "MALE") 3.5 else 3.0
    }
}