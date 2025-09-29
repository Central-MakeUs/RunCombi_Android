package com.combo.runcombi.setting.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title3
import com.combo.runcombi.setting.model.PetInfoData
import com.combo.runcombi.setting.viewmodel.AddPetViewModel
import com.combo.runcombi.setting.viewmodel.PetInfoViewModel
import com.combo.runcombi.ui.ext.clickableWithoutRipple
import com.combo.runcombi.ui.ext.screenDefaultPadding

@Composable
fun AddPetInfoScreen(
    onNext: () -> Unit,
    addPetViewModel: AddPetViewModel,
    petInfoViewModel: PetInfoViewModel = hiltViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    val uiState by petInfoViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .background(Grey01)
            .clickableWithoutRipple {
                keyboardController?.hide()
                localFocusManager.clearFocus()
            }
            .screenDefaultPadding()
    ) {
        Spacer(Modifier.height(38.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "나이", style = title3, color = Grey08)
            Spacer(modifier = Modifier.weight(1f))
            RunCombiTextField(
                value = uiState.age,
                maxLength = 2,
                placeholder = "5",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                onValueChange = { petInfoViewModel.onAgeChange(it) },
                modifier = Modifier.width(134.dp),
                isError = uiState.isAgeError,
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
                maxLength = 4,
                placeholder = "5.5",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                onValueChange = { petInfoViewModel.onWeightChange(it) },
                modifier = Modifier.width(134.dp),
                isError = uiState.isWeightError,
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
                    addPetViewModel.setPetInfo(
                        PetInfoData(
                            petAge = uiState.age.toIntOrNull(),
                            petWeight = uiState.weight.toDoubleOrNull()
                        )
                    )
                    onNext()
                }
            }, text = "다음", enabled = uiState.isButtonEnabled
        )
    }
}
