package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.LottieImage
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.feature.signup.R
import com.combo.runcombi.signup.SignupViewModel

@Composable
fun CompleteScreen(onDone: () -> Unit, viewModel: SignupViewModel = hiltViewModel()) {
    Box {
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 39.dp)) {
            Spacer(Modifier.weight(1f))
            RunCombiButton(
                onClick = onDone,
                text = "시작",
            )
        }
        LottieImage(
            modifier = Modifier.fillMaxSize(),
            lottieRes = R.raw.animation_celebration
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCompleteScreen() {
    CompleteScreen(onDone = {})
} 