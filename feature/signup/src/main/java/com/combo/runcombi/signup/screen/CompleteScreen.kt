package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.combo.runcombi.signup.SignupViewModel
import com.combo.runcombi.signup.component.SignupButton

@Composable
fun CompleteScreen(onDone: () -> Unit, viewModel: SignupViewModel) {
    Column {
        Spacer(Modifier.weight(1f))
        SignupButton(
            onClick = onDone,
            text = "시작",
        )
    }
} 