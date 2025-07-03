package com.combo.runcombi.signup.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InputStepScaffold(
    currentStep: Int,
    title: String,
    totalSteps: Int = 3,
    onBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column {
        InputStepTopBar(
            title = title,
            onBack = onBack,
            totalSteps = totalSteps,
            currentStep = currentStep
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInputStepScaffold() {
    InputStepScaffold(currentStep = 1, title = "사용자 정보", onBack = {}) {

    }
} 