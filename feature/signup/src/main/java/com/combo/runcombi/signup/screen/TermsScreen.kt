package com.combo.runcombi.signup.screen

import TermsViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.heading1
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.signup.component.AgreementItem

@Composable
fun TermsScreen(
    onNext: () -> Unit,
    viewModel: TermsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 39.dp, top = 84.dp)) {
        Text("서비스 이용을 위한 동의 안내", color = WhiteFF, style = heading1)
        Spacer(Modifier.height(57.dp))

        AgreementItem(
            text = "전체 동의",
            checked = uiState.allChecked,
            onCheckedChange = { viewModel.setAllChecked(!uiState.allChecked) },
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 27.dp, bottom = 32.dp),
            thickness = 0.8.dp,
            color = Color(0xFF353434)
        )

        AgreementItem(
            modifier = Modifier.padding(bottom = 27.dp),
            text = "[필수] 이용 약관 동의",
            checked = uiState.termsChecked,
            onCheckedChange = { viewModel.updateTermsChecked(!uiState.termsChecked) },
        )

        AgreementItem(
            modifier = Modifier.padding(bottom = 27.dp),
            text = "[필수] 개인정보 처리방침",
            checked = uiState.privacyChecked,
            onCheckedChange = { viewModel.updatePrivacyChecked(!uiState.privacyChecked) },
        )

        AgreementItem(
            modifier = Modifier.padding(bottom = 27.dp),
            text = "[필수] 위치기반 서비스 이용 약관",
            checked = uiState.locationChecked,
            onCheckedChange = { viewModel.updateLocationChecked(!uiState.locationChecked) },
        )

        Spacer(Modifier.weight(1f))

        RunCombiButton(
            onClick = onNext,
            text = "다음",
            enabled = uiState.allChecked
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun PreviewTermsScreen() {
    TermsScreen(onNext = {})
}
