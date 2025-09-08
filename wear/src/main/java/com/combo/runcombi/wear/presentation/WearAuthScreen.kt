package com.combo.runcombi.wear.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.wear.presentation.theme.RunCombi_AndroidTheme

@Composable
fun WearAuthScreen(
    viewModel: WearAuthViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> {
                LoadingScreen()
            }
            
            uiState.isLoggedIn && uiState.memberStatus == MemberStatus.LIVE -> {
                // 메인 화면으로 이동됨
            }
            
            uiState.isLoggedIn && uiState.memberStatus != MemberStatus.LIVE -> {
                IncompleteProfileScreen(
                    memberStatus = uiState.memberStatus,
                    onRefresh = { viewModel.refreshAuthStatus() }
                )
            }
            
            else -> {
                LoginRequiredScreen(
                    onLoginClick = { viewModel.requestMobileLogin() },
                    error = uiState.error,
                    onClearError = { viewModel.clearError() },
                    isWaitingForResponse = uiState.isWaitingForMobileResponse
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    val scrollState = rememberScrollState()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        CircularProgressIndicator()
        Text(
            text = "로그인 상태 확인 중...",
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun LoginRequiredScreen(
    onLoginClick: () -> Unit,
    error: String?,
    onClearError: () -> Unit,
    isWaitingForResponse: Boolean = false
) {
    val scrollState = rememberScrollState()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        Text(
            text = "RunCombi",
            textAlign = TextAlign.Center
        )
        
        if (isWaitingForResponse) {
            Text(
                text = "모바일 앱 응답 대기 중...",
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            
            CircularProgressIndicator(
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            Text(
                text = "모바일 앱에서 로그인해주세요",
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        
        if (error != null) {
            Text(
                text = error,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        if (!isWaitingForResponse) {
            RunCombiButton(
                modifier = Modifier.padding(horizontal = 30.dp),
                text = "로그인",
                onClick = {
                    onClearError()
                    onLoginClick()
                }
            )
        }
    }
}

@Composable
private fun IncompleteProfileScreen(
    memberStatus: MemberStatus,
    onRefresh: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        Text(
            text = "프로필 설정 필요",
            textAlign = TextAlign.Center
        )
        
        val message = when (memberStatus) {
            MemberStatus.PENDING_AGREE -> "약관 동의가 필요합니다"
            MemberStatus.PENDING_MEMBER_DETAIL -> "회원 정보 입력이 필요합니다"
            MemberStatus.LIVE -> "이미 완료된 상태입니다"
        }
        
        Text(
            text = message,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        RunCombiButton(
            text = "새로고침",
            onClick = onRefresh
        )
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(device = WearDevices.SMALL_ROUND)
fun WearAuthScreenPreview() {
    RunCombi_AndroidTheme {
        LoginRequiredScreen(
            onLoginClick = {},
            error = null,
            onClearError = {}
        )
    }
}
