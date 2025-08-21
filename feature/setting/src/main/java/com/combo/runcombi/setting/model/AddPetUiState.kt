package com.combo.runcombi.setting.model

import com.combo.runcombi.domain.user.model.RunStyle

data class PetProfileUiState(
    val name: String = "",
    val isError: Boolean = false,
    val isButtonEnabled: Boolean = false
)

data class PetInfoUiState(
    val age: String = "",
    val weight: String = "",
    val isButtonEnabled: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isAgeError: Boolean = false,
    val isWeightError: Boolean = false
)

data class PetStyleUiState(
    val selectedStyle: RunStyle? = null,
    val isButtonEnabled: Boolean = false,
    val isLoading: Boolean = false
) 