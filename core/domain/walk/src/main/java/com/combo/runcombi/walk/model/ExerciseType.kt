package com.combo.runcombi.walk.model

enum class ExerciseType {
    WALKING,
    FAST_WALKING,
    JOGGING;

    fun getMet(gender: String): Double = when (this) {
        WALKING -> if (gender == "MALE") 3.5 else 3.0
        FAST_WALKING -> if (gender == "MALE") 4.8 else 4.2
        JOGGING -> if (gender == "MALE") 7.5 else 7.0
    }
} 