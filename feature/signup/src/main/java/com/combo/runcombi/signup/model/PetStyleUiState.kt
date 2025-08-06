package com.combo.runcombi.signup.model

import com.combo.runcombi.domain.user.model.RunStyle


data class PetStyleUiState(
    val selectedStyle: RunStyle? = null,
    val isButtonEnabled: Boolean = false,
    val isLoading: Boolean = false
)