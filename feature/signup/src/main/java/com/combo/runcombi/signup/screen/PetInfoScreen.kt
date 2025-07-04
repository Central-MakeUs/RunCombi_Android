package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.ext.clickableWithoutRipple
import com.combo.runcombi.core.designsystem.ext.screenDefaultPadding
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title3
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.feature.signup.R
import com.combo.runcombi.signup.SignupViewModel
import com.combo.runcombi.signup.model.PetInfoData

@Composable
fun PetInfoScreen(
    onNext: () -> Unit,
    viewModel: SignupViewModel = hiltViewModel(),
    petInfoViewModel: PetInfoViewModel = hiltViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.clearPetInfo()
        petInfoViewModel.clear()
    }

    val uiState by petInfoViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .clickableWithoutRipple {
                keyboardController?.hide()
                localFocusManager.clearFocus()
            }
            .screenDefaultPadding()) {
        Text(
            text = "반려견 정보를 알려주세요",
            style = title2,
            color = WhiteFF,
            modifier = Modifier.padding(top = 38.dp, bottom = 9.dp)
        )
        Text(
            text = "외부에 공개되지 않아요", style = body2, color = Grey06,
        )
        Spacer(Modifier.height(78.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "나이", style = title3, color = Grey08)
            Spacer(modifier = Modifier.weight(1f))
            RunCombiTextField(
                value = uiState.age,
                maxLength = 2,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                onValueChange = { petInfoViewModel.onAgeChange(it) },
                modifier = Modifier.width(134.dp),
                isError = uiState.isError && uiState.errorMessage.contains("나이"),
                visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                enabled = true,
                singleLine = true,
                trailingText = "살"
            )
        }
        Spacer(Modifier.height(27.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "몸무게", style = title3, color = Grey08)
            Spacer(modifier = Modifier.weight(1f))
            RunCombiTextField(
                value = uiState.weight,
                maxLength = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                onValueChange = { petInfoViewModel.onWeightChange(it) },
                modifier = Modifier.width(134.dp),
                isError = uiState.isError && uiState.errorMessage.contains("몸무게"),
                visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                enabled = true,
                singleLine = true,
                trailingText = "kg"
            )
        }
        Spacer(Modifier.weight(1f))
        RunCombiButton(
            onClick = {
                petInfoViewModel.validateAndProceed {
                    keyboardController?.hide()
                    localFocusManager.clearFocus()
                    viewModel.setPetInfo(
                        PetInfoData(
                            petAge = uiState.age.toIntOrNull(),
                            petWeight = uiState.weight.toIntOrNull()
                        )
                    )
                    onNext()
                }
            }, text = stringResource(R.string.next), enabled = uiState.isButtonEnabled
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun PreviewPetInfoScreen() {
    PetInfoScreen(onNext = {})
} 