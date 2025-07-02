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

@Composable
fun PetStyleScreen(onSuccess: () -> Unit, viewModel: SignupViewModel) {
    LaunchedEffect(Unit) {
        viewModel.clearPetStyle()
    }
    Column {
        Spacer(Modifier.weight(1f))
        SignupButton(
            onClick = onSuccess,
            text = "완료",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPetStyleScreen() {
    PetStyleScreen(onSuccess = {}, viewModel = androidx.lifecycle.viewmodel.compose.viewModel())
} 