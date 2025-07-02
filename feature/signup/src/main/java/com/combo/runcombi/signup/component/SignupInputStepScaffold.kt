package com.combo.runcombi.signup.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun SignupInputStepScaffold(
    currentStep: Int,
    onBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column {
        SignupTopBar(
            title = "사용자 정보",
            onBack = onBack,
            totalSteps = 5,
            currentStep = currentStep
        )
        content()
    }
} 