package com.combo.runcombi.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.domain.user.model.UserInfo
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.wear.auth.WearTokenManager
import com.combo.runcombi.wear.data.WearDataSyncService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val wearTokenManager: WearTokenManager,
    private val wearDataSyncService: WearDataSyncService
) : ViewModel() {

    private val _uiState = MutableStateFlow(WearAuthUiState())
    val uiState: StateFlow<WearAuthUiState> = _uiState.asStateFlow()

    init {
        // 모바일로부터 토큰 요청
        requestTokenFromMobile()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun requestTokenFromMobile() {
        android.util.Log.d("WearAuthViewModel", "=== 고정 토큰 사용 시작 ===")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isWaitingForMobileResponse = true,
                error = null
            )
            
            try {
                // 고정 토큰 사용
                val fixedToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3NTkxNDg3ODYsInN1YiI6IjEzMiIsImV4cCI6MTc1OTc1MzU4Niwicm9sZSI6IlVTRVIifQ.rlUVKbI7BFqcm3W2JryWvqVtOt2PtYiS_qoz7Elcw_hgzlYiwWjaZlAZk1PAwRJmhl0VdddFVOzhd-mle6rRQA"
                
                android.util.Log.d("WearAuthViewModel", "고정 토큰 설정 중...")
                android.util.Log.d("WearAuthViewModel", "토큰: ${fixedToken.take(20)}...")
                
                // 고정 토큰을 로컬 저장소에 저장
                wearTokenManager.saveAccessTokenFromMobile(fixedToken)
                android.util.Log.d("WearAuthViewModel", "고정 토큰 저장 완료")
                
                // 잠시 대기
                kotlinx.coroutines.delay(1000)
                
                android.util.Log.d("WearAuthViewModel", "=== 고정 토큰 사용 성공 ===")
                loadUserInfoWithToken()
                
            } catch (e: Exception) {
                android.util.Log.e("WearAuthViewModel", "=== 고정 토큰 사용 중 오류 ===", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isWaitingForMobileResponse = false,
                    error = "고정 토큰 사용 중 오류 발생: ${e.message}"
                )
            }
        }
    }

    private fun loadUserInfoWithToken() {
        viewModelScope.launch {
            try {
                android.util.Log.d("WearAuthViewModel", "토큰으로 유저 정보 조회 시작")
                val result = getUserInfoUseCase().first()
                
                when (result) {
                    is DomainResult.Success -> {
                        android.util.Log.d("WearAuthViewModel", "유저 정보 조회 성공")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            userInfo = result.data,
                            memberStatus = result.data.memberStatus,
                            isWaitingForMobileResponse = false
                        )
                    }
                    is DomainResult.Error -> {
                        android.util.Log.e("WearAuthViewModel", "유저 정보 조회 실패: ${result.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            error = "유저 정보 조회 실패: ${result.message}",
                            isWaitingForMobileResponse = false
                        )
                    }
                    else -> {
                        android.util.Log.e("WearAuthViewModel", "알 수 없는 오류")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            error = "알 수 없는 오류",
                            isWaitingForMobileResponse = false
                        )
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("WearAuthViewModel", "유저 정보 조회 중 오류 발생", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    error = "유저 정보 조회 중 오류 발생: ${e.message}",
                    isWaitingForMobileResponse = false
                )
            }
        }
    }

}
