package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.ext.screenDefaultPadding
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.feature.signup.R
import com.combo.runcombi.signup.SignupViewModel
import com.combo.runcombi.signup.model.Gender
import com.combo.runcombi.signup.model.GenderData

@Composable
fun GenderScreen(onNext: () -> Unit, viewModel: SignupViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.clearGender()
    }
    Column(modifier = Modifier.screenDefaultPadding()) {
        Text(
            text = "성별이 어떻게 되시나요?",
            style = title2,
            color = WhiteFF,
            modifier = Modifier.padding(top = 38.dp, bottom = 9.dp)
        )
        Text(
            text = "외부에 공개되지 않아요", style = body2, color = Grey06,
        )
        Spacer(Modifier.weight(1f))
        RunCombiButton(
            onClick = {
                viewModel.setGender(data = GenderData(gender = Gender.MALE.name))
                onNext()
            },
            textColor = WhiteFF,
            enabledColor = Grey04,
            text = stringResource(R.string.male),
        )
        Spacer(Modifier.height(14.dp))
        RunCombiButton(
            onClick = {
                viewModel.setGender(data = GenderData(gender = Gender.FEMALE.name))
                onNext()
            },
            textColor = WhiteFF,
            enabledColor = Grey04,
            text = stringResource(R.string.female),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun PreviewGenderScreen() {
    GenderScreen(onNext = {})
} 