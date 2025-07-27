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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.ui.ext.screenDefaultPadding
import com.combo.runcombi.core.designsystem.theme.Grey03
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.heading2
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.feature.signup.R
import com.combo.runcombi.signup.model.GenderData
import com.combo.runcombi.signup.viewmodel.GenderViewModel
import com.combo.runcombi.signup.viewmodel.SignupViewModel

@Composable
fun GenderScreen(
    onNext: () -> Unit,
    genderViewModel: GenderViewModel = hiltViewModel(),
    signupViewModel: SignupViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        signupViewModel.clearGender()
    }
    val selectedGender by genderViewModel.selectedGender.collectAsStateWithLifecycle()
    Column(modifier = Modifier.screenDefaultPadding()) {
        Text(
            text = "성별이 어떻게 되시나요?",
            style = heading2,
            color = WhiteFF,
            modifier = Modifier.padding(top = 38.dp, bottom = 9.dp)
        )
        Text(
            text = "외부에 공개되지 않아요", style = body1, color = Grey06,
        )
        Spacer(Modifier.weight(1f))
        RunCombiButton(
            onClick = {
                genderViewModel.selectGender(Gender.MALE)
                signupViewModel.setGender(GenderData(gender = Gender.MALE))
                onNext()
            },
            textColor = if (selectedGender == Gender.MALE) Grey03 else WhiteFF,
            enabledColor = if (selectedGender == Gender.MALE) Primary01 else Grey04,
            text = stringResource(R.string.male),
        )
        Spacer(Modifier.height(14.dp))
        RunCombiButton(
            onClick = {
                genderViewModel.selectGender(Gender.FEMALE)
                signupViewModel.setGender(GenderData(gender = Gender.FEMALE))
                onNext()
            },
            textColor = if (selectedGender == Gender.FEMALE) Grey03 else WhiteFF,
            enabledColor = if (selectedGender == Gender.FEMALE) Primary01 else Grey04,
            text = stringResource(R.string.female),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun PreviewGenderScreen() {
    GenderScreen(onNext = {})
} 