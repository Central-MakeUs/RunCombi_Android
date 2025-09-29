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
import com.combo.runcombi.wear.data.WearConnectionChecker
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
    private val wearDataSyncService: WearDataSyncService,
    private val wearConnectionChecker: WearConnectionChecker,
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
        android.util.Log.d("WearAuthViewModel", "=== 모바일 토큰 요청 시작 ===")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isWaitingForMobileResponse = true,
                error = null
            )
            
            try {
                // 1. Google Play Services 상태 확인
                android.util.Log.d("WearAuthViewModel", "=== 연결 상태 확인 시작 ===")
                val googlePlayServicesOk = wearConnectionChecker.checkGooglePlayServices()
                android.util.Log.d("WearAuthViewModel", "Google Play Services: $googlePlayServicesOk")
                
                if (!googlePlayServicesOk) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isWaitingForMobileResponse = false,
                        error = "Google Play Services가 사용 불가능합니다. 워치를 다시 시작해주세요."
                    )
                    return@launch
                }
                
                // 2. Wearable 연결 상태 확인
                val wearableConnected = wearConnectionChecker.checkWearableConnection()
                android.util.Log.d("WearAuthViewModel", "Wearable 연결: $wearableConnected")
                
                if (!wearableConnected) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isWaitingForMobileResponse = false,
                        error = "모바일과 워치가 연결되지 않았습니다. 모바일 앱이 실행 중인지 확인해주세요."
                    )
                    return@launch
                }
                
                // 3. Capability 확인
                val capabilityOk = wearConnectionChecker.checkCapability()
                android.util.Log.d("WearAuthViewModel", "Capability: $capabilityOk")
                
                android.util.Log.d("WearAuthViewModel", "모바일로 로그인 요청 전송 중...")
                wearDataSyncService.requestMobileLogin()
                android.util.Log.d("WearAuthViewModel", "모바일로 로그인 요청 전송 완료")
                
                android.util.Log.d("WearAuthViewModel", "모바일 응답 대기 중... (3초)")
                kotlinx.coroutines.delay(3000)
                
                android.util.Log.d("WearAuthViewModel", "로컬 저장소에서 토큰 확인 중...")
                val accessToken = wearTokenManager.getAccessToken()
                android.util.Log.d("WearAuthViewModel", "토큰 확인 결과: ${accessToken != null}")
                
                if (accessToken != null) {
                    android.util.Log.d("WearAuthViewModel", "=== 토큰 수신 성공 ===")
                    android.util.Log.d("WearAuthViewModel", "토큰: ${accessToken.take(20)}...")
                    loadUserInfoWithToken()
                } else {
                    android.util.Log.w("WearAuthViewModel", "=== 토큰 수신 실패 ===")
                    android.util.Log.w("WearAuthViewModel", "모바일 앱이 실행 중인지 확인 필요")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isWaitingForMobileResponse = false,
                        error = "모바일로부터 토큰을 받지 못했습니다. 모바일 앱이 실행 중인지 확인해주세요."
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("WearAuthViewModel", "=== 모바일 토큰 요청 중 오류 ===", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isWaitingForMobileResponse = false,
                    error = "모바일 토큰 요청 중 오류 발생: ${e.message}"
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
