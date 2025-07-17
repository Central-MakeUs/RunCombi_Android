package com.combo.runcombi.signup.model

data class TermsUiModel(
    val termsChecked: Boolean = false,
    val privacyChecked: Boolean = false,
    val locationChecked: Boolean = false
)

data class TermsUiState(
    val uiModel: TermsUiModel = TermsUiModel(),
    val isLoading: Boolean = false,
) {
    val allChecked: Boolean
        get() = uiModel.termsChecked && uiModel.privacyChecked && uiModel.locationChecked
}
