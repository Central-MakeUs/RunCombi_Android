package com.combo.runcombi.onboarding

import androidx.lifecycle.ViewModel
import com.combo.runcombi.onboarding.model.OnboardingUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel() : ViewModel() {
    private val _onboardingUiModel = MutableStateFlow<OnboardingUiModel>(OnboardingUiModel.Loading)
    val onboardingUiModel: StateFlow<OnboardingUiModel> = _onboardingUiModel.asStateFlow()

}
