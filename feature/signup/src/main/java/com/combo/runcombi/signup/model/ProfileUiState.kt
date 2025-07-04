package com.combo.runcombi.signup.model

data class ProfileUiState(
    val name: String = "",
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isButtonEnabled: Boolean = false,
)