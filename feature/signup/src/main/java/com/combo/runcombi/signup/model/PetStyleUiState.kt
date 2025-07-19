package com.combo.runcombi.signup.model

import com.combo.runcombi.domain.user.model.RunStyle


data class PetStyleUiState(
    val selectedStyle: RunStyle = RunStyle.RUNNING,
    val isButtonEnabled: Boolean = true,
    val isLoading: Boolean = false
)