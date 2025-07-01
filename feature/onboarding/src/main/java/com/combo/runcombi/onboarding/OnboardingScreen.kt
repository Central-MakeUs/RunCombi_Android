package com.combo.runcombi.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.onboarding.model.OnboardingUiModel


@Composable
fun OnboardingRoute(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val onboardingUiModel by viewModel.onboardingUiModel.collectAsStateWithLifecycle()

    OnboardingScreen(
        onboardingUiModel = onboardingUiModel,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
fun OnboardingScreen(
    onboardingUiModel: OnboardingUiModel,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(Color.Black)
    ) {}
}


@Preview
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(
        onboardingUiModel = OnboardingUiModel.Success
    )
}

