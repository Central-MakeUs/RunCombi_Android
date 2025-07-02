package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.combo.runcombi.signup.SignupViewModel
import com.combo.runcombi.signup.component.SignupButton
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BodyScreen(onNext: () -> Unit, viewModel: SignupViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.clearBody()
    }
    Column {
        Spacer(Modifier.weight(1f))
        SignupButton(
            onClick = onNext,
            text = "다음",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBodyScreen() {
    BodyScreen(onNext = {})
} 