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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.heading2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title3
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.feature.signup.R
import com.combo.runcombi.signup.model.BodyData
import com.combo.runcombi.signup.viewmodel.BodyViewModel
import com.combo.runcombi.signup.viewmodel.SignupViewModel
import com.combo.runcombi.ui.ext.clickableWithoutRipple
import com.combo.runcombi.ui.ext.screenDefaultPadding

@Composable
fun BodyScreen(
    onNext: () -> Unit,
    signupViewModel: SignupViewModel = hiltViewModel(),
    bodyViewModel: BodyViewModel = hiltViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        signupViewModel.clearBody()
    }

    val signupData by signupViewModel.signupData.collectAsStateWithLifecycle()
    val gender = signupData.genderData.gender

    LaunchedEffect(gender) {
        bodyViewModel.setDefaultValues(gender)
    }

    val uiState by bodyViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .clickableWithoutRipple {
                keyboardController?.hide()
                localFocusManager.clearFocus()
            }
            .screenDefaultPadding()) {
        Text(
            text = "신체 정보를 알려주세요",
            style = heading2,
            color = WhiteFF,
            modifier = Modifier.padding(top = 38.dp, bottom = 9.dp)
        )
        Text(
            text = "외부에 공개되지 않아요", style = body1, color = Grey06,
        )
        Spacer(Modifier.height(78.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "키", style = title3, color = Grey08)
            Spacer(modifier = Modifier.weight(1f))
            RunCombiTextField(
                value = uiState.height,
                maxLength = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                onValueChange = { bodyViewModel.onHeightChange(it, gender) },
                modifier = Modifier
                    .width(134.dp)
                    .height(40.dp),
                isError = uiState.isError && uiState.errorMessage.contains("키"),
                visualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
                enabled = true,
                singleLine = true,
                trailingText = "cm"
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
                onValueChange = { bodyViewModel.onWeightChange(it, gender) },
                modifier = Modifier
                    .width(134.dp)
                    .height(40.dp),
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
                bodyViewModel.validateAndProceed(gender) {
                    keyboardController?.hide()
                    localFocusManager.clearFocus()
                    signupViewModel.setBody(
                        BodyData(
                            height = uiState.height.toIntOrNull(),
                            weight = uiState.weight.toIntOrNull()
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
fun PreviewBodyScreen() {
    BodyScreen(onNext = {})
} 