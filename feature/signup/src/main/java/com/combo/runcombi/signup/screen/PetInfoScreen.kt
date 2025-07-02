package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import com.combo.runcombi.signup.SignupViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.combo.runcombi.signup.component.SignupButton
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PetInfoScreen(onNext: () -> Unit, viewModel: SignupViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.clearPetInfo()
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
fun PreviewPetInfoScreen() {
    PetInfoScreen(onNext = {})
} 