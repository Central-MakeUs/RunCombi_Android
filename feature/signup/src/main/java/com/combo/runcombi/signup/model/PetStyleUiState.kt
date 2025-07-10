package com.combo.runcombi.signup.model

data class PetStyleUiState(
    val selectedStyle: PetStyleType = PetStyleType.ENERGETIC,
    val isButtonEnabled: Boolean = true,
)

enum class PetStyleType {
    ENERGETIC, // 에너지가 넘쳐요!
    RELAXED,   // 여유롭게 걸어요
    SLOW       // 천천히 걸으며 자주 쉬어요
} 