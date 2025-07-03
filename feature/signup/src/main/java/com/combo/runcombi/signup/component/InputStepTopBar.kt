package com.combo.runcombi.signup.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar

@Composable
fun InputStepTopBar(
    title: String = "사용자 정보",
    onBack: () -> Unit,
    totalSteps: Int? = null,
    currentStep: Int? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        RunCombiAppTopBar(title = title, onBack = onBack)

        if (totalSteps != null && currentStep != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(totalSteps) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                if (index <= currentStep) Color(0xFFD7FE63) else Color(0xFF1E1F23)
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInputStepSignupTopBar() {
    InputStepTopBar(title = "회원가입", onBack = {}, totalSteps = 5, currentStep = 2)
} 