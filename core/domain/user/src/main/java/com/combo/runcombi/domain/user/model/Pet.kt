package com.combo.runcombi.domain.user.model

data class Pet(
    val name: String,
    val age: Int,
    val weight: Double,
    val runStyle: RunStyle,
    val profileImageUrl: String? = null,
)

/**
 * RUNNING - 에너지가 넘쳐요
 * WALKING - 여유롭게 걸어요
 * SLOW_WALKING - 천천히 걸으며 자주 쉬어요
 * */
enum class RunStyle {
    RUNNING,
    WALKING,
    SLOW_WALKING
}
