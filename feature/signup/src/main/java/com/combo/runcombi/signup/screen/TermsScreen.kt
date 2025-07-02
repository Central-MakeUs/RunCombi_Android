package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import com.combo.runcombi.signup.SignupViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.combo.runcombi.signup.component.SignupButton

@Composable
fun TermsScreen(onNext: () -> Unit, viewModel: SignupViewModel) {
    Column {
        Spacer(Modifier.weight(1f))
        SignupButton(
            onClick = onNext,
            text = "다음",
        )
    }
} 