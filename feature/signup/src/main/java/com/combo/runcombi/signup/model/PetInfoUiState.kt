package com.combo.runcombi.signup.model

data class PetInfoUiState(
    val age: String = "",
    val weight: String = "",
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isButtonEnabled: Boolean = false,
) 