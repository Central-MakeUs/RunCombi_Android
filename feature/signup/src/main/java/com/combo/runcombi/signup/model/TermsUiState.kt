package com.combo.runcombi.signup.model

data class TermsUiState(
    val termsChecked: Boolean = false,
    val privacyChecked: Boolean = false,
    val locationChecked: Boolean = false,
) {
    val allChecked: Boolean
        get() = termsChecked && privacyChecked && locationChecked
}
