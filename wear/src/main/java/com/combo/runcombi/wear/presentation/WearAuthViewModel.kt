package com.combo.runcombi.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.model.UserInfo
import com.combo.runcombi.wear.auth.WearAuthManager
import com.combo.runcombi.wear.data.WearDataSyncService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WearAuthUiState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
    val userInfo: UserInfo? = null,
    val memberStatus: MemberStatus = MemberStatus.PENDING_AGREE,
    val error: String? = null,
    val isWaitingForMobileResponse: Boolean = false
)

@HiltViewModel
class WearAuthViewModel @Inject constructor(
    private val wearAuthManager: WearAuthManager,
    private val wearDataSyncService: WearDataSyncService
) : ViewModel() {

    private val _uiState = MutableStateFlow(WearAuthUiState())
    val uiState: StateFlow<WearAuthUiState> = _uiState.asStateFlow()

    init {
        checkAuthStatus()
        wearAuthManager.setOnTokenReceivedCallback {
            onTokenReceived()
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val isLoggedIn = wearAuthManager.isLoggedIn()
                
                if (isLoggedIn) {
                    val memberStatus = wearAuthManager.getUserStatus()
                    val userInfo = if (memberStatus == MemberStatus.LIVE) {
                        wearAuthManager.getUserInfo()
                    } else null
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        userInfo = userInfo,
                        memberStatus = memberStatus
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun requestMobileLogin() {
        viewModelScope.launch {
            try {
                android.util.Log.d("WearAuthViewModel", "Requesting mobile login...")
                _uiState.value = _uiState.value.copy(isWaitingForMobileResponse = true)
                
                wearDataSyncService.requestMobileLogin()
                android.util.Log.d("WearAuthViewModel", "Mobile login request sent")
                
                // 10초 후 응답이 없으면 타임아웃 처리
                kotlinx.coroutines.delay(10000)
                if (_uiState.value.isWaitingForMobileResponse) {
                    _uiState.value = _uiState.value.copy(
                        isWaitingForMobileResponse = false,
                        error = "모바일 앱이 실행되지 않았거나 연결되지 않았습니다"
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("WearAuthViewModel", "Failed to request mobile login", e)
                _uiState.value = _uiState.value.copy(
                    isWaitingForMobileResponse = false,
                    error = "모바일 로그인 요청 실패: ${e.message}"
                )
            }
        }
    }

    fun refreshAuthStatus() {
        checkAuthStatus()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun onTokenReceived() {
        _uiState.value = _uiState.value.copy(
            isWaitingForMobileResponse = false
        )
        // 토큰을 받았으므로 인증 상태 다시 확인
        checkAuthStatus()
    }
}
