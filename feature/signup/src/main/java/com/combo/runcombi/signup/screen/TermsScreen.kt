package com.combo.runcombi.signup.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.ext.screenDefaultPadding
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.heading1
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.feature.signup.R
import com.combo.runcombi.signup.component.AgreementItem
import com.combo.runcombi.signup.model.TermsEvent
import com.combo.runcombi.signup.viewmodel.TermsViewModel

@Composable
fun TermsScreen(
    onNext: () -> Unit,
    viewModel: TermsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val eventFlow = viewModel.eventFlow

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is TermsEvent.Success -> {
                    viewModel.checkUserStatus()
                }

                is TermsEvent.Error -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is TermsEvent.NavigateNext -> {
                    onNext()
                }
            }
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary01)
        }
    }

    Column(
        modifier = Modifier
            .screenDefaultPadding()
            .padding(top = 89.dp)
    ) {
        Text("서비스 이용을 위한\n동의 안내", color = WhiteFF, style = heading1)

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
            text = "[필수] 서비스 이용약관",
            checked = uiState.uiModel.termsChecked,
            onCheckedChange = { viewModel.updateTermsChecked(!uiState.uiModel.termsChecked) },
        )

        AgreementItem(
            modifier = Modifier.padding(bottom = 27.dp),
            text = "[필수] 개인정보처리방침",
            checked = uiState.uiModel.privacyChecked,
            onCheckedChange = { viewModel.updatePrivacyChecked(!uiState.uiModel.privacyChecked) },
        )

        AgreementItem(
            modifier = Modifier.padding(bottom = 27.dp),
            text = "[필수] 위치정보 이용약관",
            checked = uiState.uiModel.locationChecked,
            onCheckedChange = { viewModel.updateLocationChecked(!uiState.uiModel.locationChecked) },
        )

        Spacer(Modifier.weight(1f))

        RunCombiButton(
            onClick = {
                viewModel.setAgreeTerms()
            },
            text = stringResource(R.string.next),
            enabled = uiState.allChecked
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun PreviewTermsScreen() {
    TermsScreen(onNext = {})
}
