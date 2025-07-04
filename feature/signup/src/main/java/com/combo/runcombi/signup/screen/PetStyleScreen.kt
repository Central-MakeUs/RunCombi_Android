package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiSelectableButton
import com.combo.runcombi.core.designsystem.ext.clickableWithoutRipple
import com.combo.runcombi.core.designsystem.ext.screenDefaultPadding
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.signup.model.PetStyleType

@Composable
fun PetStyleScreen(onSuccess: () -> Unit, viewModel: PetStyleViewModel = hiltViewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearStyle()
    }
    Column(
        modifier = Modifier
            .clickableWithoutRipple {
                keyboardController?.hide()
                localFocusManager.clearFocus()
            }
            .screenDefaultPadding()) {
        Text(
            text = "산책스타일을 알려주세요",
            style = title2,
            color = WhiteFF,
            modifier = Modifier.padding(top = 38.dp, bottom = 9.dp)
        )
        Text(
            text = "더 정확한 반려견 소모 칼로리 계산을 위해 필요해요", style = body2, color = Grey06,
        )
        Spacer(Modifier.height(49.dp))
        Text(
            text = "산책스타일", style = body3, color = Grey08,
        )
        Spacer(Modifier.height(5.dp))
        RunCombiSelectableButton(
            text = "에너지가 넘쳐요!",
            isSelected = uiState.selectedStyle == PetStyleType.ENERGETIC,
            onClick = { viewModel.selectStyle(PetStyleType.ENERGETIC) },
        )
        Spacer(Modifier.height(14.dp))
        RunCombiSelectableButton(
            text = "여유롭게 걸어요",
            isSelected = uiState.selectedStyle == PetStyleType.RELAXED,
            onClick = { viewModel.selectStyle(PetStyleType.RELAXED) },
        )
        Spacer(Modifier.height(14.dp))
        RunCombiSelectableButton(
            text = "천천히 걸으며 자주 쉬어요",
            isSelected = uiState.selectedStyle == PetStyleType.SLOW,
            onClick = { viewModel.selectStyle(PetStyleType.SLOW) },
        )

        Spacer(Modifier.weight(1f))
        RunCombiButton(
            onClick = {
                keyboardController?.hide()
                localFocusManager.clearFocus()
                onSuccess()
            },
            text = "완료",
            enabled = uiState.isButtonEnabled
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPetStyleScreen() {
    PetStyleScreen(onSuccess = {})
} 