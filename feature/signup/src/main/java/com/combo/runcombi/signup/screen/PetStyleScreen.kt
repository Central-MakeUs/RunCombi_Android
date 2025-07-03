package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.signup.SignupViewModel

@Composable
fun PetStyleScreen(onSuccess: () -> Unit, viewModel: SignupViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.clearPetStyle()
    }
    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 39.dp)) {
        Spacer(Modifier.weight(1f))
        RunCombiButton(
            onClick = onSuccess,
            text = "완료",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPetStyleScreen() {
    PetStyleScreen(onSuccess = {})
} 