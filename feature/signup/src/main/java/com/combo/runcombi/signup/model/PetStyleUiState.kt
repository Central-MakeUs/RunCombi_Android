package com.combo.runcombi.signup.model

// 산책 스타일 선택 상태를 관리하는 데이터 클래스

data class PetStyleUiState(
    val selectedStyle: PetStyleType? = null,
    val isButtonEnabled: Boolean = false
)

// 산책 스타일 종류

enum class PetStyleType {
    ENERGETIC, // 에너지가 넘쳐요!
    RELAXED,   // 여유롭게 걸어요
    SLOW       // 천천히 걸으며 자주 쉬어요
} 