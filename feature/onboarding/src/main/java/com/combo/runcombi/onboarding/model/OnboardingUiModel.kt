package com.combo.runcombi.onboarding.model

sealed class OnboardingUiModel {
    data object Loading : OnboardingUiModel()
    data object Success : OnboardingUiModel()
}
