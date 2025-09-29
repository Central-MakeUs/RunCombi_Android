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
    private val wearTokenManager: WearTokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(WearAuthUiState())
    val uiState: StateFlow<WearAuthUiState> = _uiState.asStateFlow()

    init {
        // 하드코딩된 토큰 초기화 및 유저 정보 조회
        initializeTokensAndLoadUserInfo()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun initializeTokensAndLoadUserInfo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // 하드코딩된 토큰 초기화
                android.util.Log.d("WearAuthViewModel", "토큰 초기화 시작")
                wearTokenManager.initializeTokens()
                android.util.Log.d("WearAuthViewModel", "토큰 초기화 완료")
                
                // 실제 API로 유저 정보 조회
                android.util.Log.d("WearAuthViewModel", "유저 정보 API 호출 시작")
                val result = getUserInfoUseCase().first()
                android.util.Log.d("WearAuthViewModel", "유저 정보 API 호출 완료: $result")
                
                // 결과 타입별 상세 로그
                when (result) {
                    is DomainResult.Success -> {
                        android.util.Log.d("WearAuthViewModel", "Success - 데이터: ${result.data}")
                    }
                    is DomainResult.Error -> {
                        android.util.Log.e("WearAuthViewModel", "Error - 코드: ${result.code}, 메시지: ${result.message}")
                    }
                    is DomainResult.Exception -> {
                        android.util.Log.e("WearAuthViewModel", "Exception - 에러: ${result.error}")
                    }
                }
                
                when (result) {
                    is DomainResult.Success -> {
                        android.util.Log.d("WearAuthViewModel", "유저 정보 조회 성공: ${result.data}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            userInfo = result.data,
                            memberStatus = result.data.memberStatus
                        )
                    }
                    is DomainResult.Error -> {
                        android.util.Log.e("WearAuthViewModel", "유저 정보 조회 실패: ${result.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            error = "유저 정보 조회 실패: ${result.message}"
                        )
                    }
                    else -> {
                        android.util.Log.e("WearAuthViewModel", "알 수 없는 오류: $result")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            error = "알 수 없는 오류"
                        )
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("WearAuthViewModel", "유저 정보 조회 중 오류 발생", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    error = "유저 정보 조회 중 오류 발생: ${e.message}"
                )
            }
        }
    }
}
