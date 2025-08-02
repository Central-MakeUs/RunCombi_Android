package com.combo.runcombi.setting.model

import com.combo.runcombi.domain.user.model.Gender

data class EditMemberUiState(
    val name: String = "",
    val gender: Gender = Gender.FEMALE,
    val profileImageUrl: String = "",
    val height: String = "",
    val weight: String = "",
    val isNameError: Boolean = false,
    val isHeightError: Boolean = false,
    val isWeightError: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
)