package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.combo.runcombi.core.designsystem.component.LottieImage
import com.combo.runcombi.feature.signup.R
import com.combo.runcombi.signup.SignupViewModel
import com.combo.runcombi.signup.component.SignupButton
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CompleteScreen(onDone: () -> Unit, viewModel: SignupViewModel = hiltViewModel()) {
    Box {
        Column {
            Spacer(Modifier.weight(1f))
            SignupButton(
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