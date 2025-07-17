package com.combo.runcombi.domain.user.model

data class Pet(
    val name: String,
    val age: Int,
    val weight: Double,
    val runStyle: RunStyle,
    val profileImageUrl: String? = null,
)

enum class RunStyle {
    ENERGETIC,
    RELAXED,
    SLOW
}
