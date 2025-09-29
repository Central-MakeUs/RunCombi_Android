package com.combo.runcombi.wear.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
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
            .padding(8.dp),
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
                    onRefresh = { /* TODO: 새로고침 기능 */ }
                )
            }
            
            else -> {
                LoginRequiredScreen(
                    onLoginClick = { 
                        android.util.Log.d("WearAuthScreen", "로그인 버튼 클릭됨")
                        viewModel.requestTokenFromMobile() 
                    },
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
    val listState = rememberScalingLazyListState()
    
    ScalingLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            CircularProgressIndicator()
        }
        item {
            Text(
                text = "확인 중...",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun LoginRequiredScreen(
    onLoginClick: () -> Unit,
    error: String?,
    onClearError: () -> Unit,
    isWaitingForResponse: Boolean = false
) {
    val listState = rememberScalingLazyListState()
    
    ScalingLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "RunCombi",
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center
            )
        }
        
        if (isWaitingForResponse) {
            item {
                Text(
                    text = "응답 대기 중...",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                CircularProgressIndicator(
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        } else {
            item {
                Text(
                    text = "모바일 앱에서 로그인",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
        
        if (error != null) {
            item {
                Text(
                    text = error,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
        
        if (!isWaitingForResponse) {
            item {
                RunCombiButton(
                    text = "로그인",
                    onClick = {
                        onClearError()
                        onLoginClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun IncompleteProfileScreen(
    memberStatus: MemberStatus,
    onRefresh: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    
    ScalingLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "프로필 설정 필요",
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center
            )
        }
        
        item {
            val message = when (memberStatus) {
                MemberStatus.PENDING_AGREE -> "약관 동의 필요"
                MemberStatus.PENDING_MEMBER_DETAIL -> "회원 정보 입력 필요"
                MemberStatus.LIVE -> "완료됨"
            }
            
            Text(
                text = message,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        item {
            RunCombiButton(
                text = "새로고침",
                onClick = onRefresh
            )
        }
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
