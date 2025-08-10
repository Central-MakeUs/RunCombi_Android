package com.combo.runcombi.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.auth.usecase.WithdrawUseCase
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.model.UserWithdrawalSurveyEvent
import com.combo.runcombi.setting.model.UserWithdrawalSurveyUiState
import com.combo.runcombi.setting.model.WithdrawalReason
import com.combo.runcombi.setting.usecase.AccountDeletionReasonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserWithdrawalSurveyViewModel @Inject constructor(
    private val accountDeletionReasonUseCase: AccountDeletionReasonUseCase,
    private val withdrawUseCase: WithdrawUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserWithdrawalSurveyUiState())
    val uiState: StateFlow<UserWithdrawalSurveyUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UserWithdrawalSurveyEvent>()
    val eventFlow: SharedFlow<UserWithdrawalSurveyEvent> = _eventFlow.asSharedFlow()

    private fun emitEvent(event: UserWithdrawalSurveyEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun updateWithdrawalReason(reason: WithdrawalReason) {
        val currentSelectedReasons = _uiState.value.selectedReason.toMutableList()

        if (currentSelectedReasons.contains(reason)) {
            // 이미 선택된 경우 제거
            currentSelectedReasons.remove(reason)
        } else {
            // 선택되지 않은 경우 추가
            currentSelectedReasons.add(reason)
        }

        _uiState.update { it.copy(selectedReason = currentSelectedReasons) }
    }

    fun updateAdditionalReason(text: String) {
        _uiState.update { it.copy(additionalReason = text) }
    }

    fun tryWithDraw() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // 선택된 사유가 없는 경우 에러 처리
                if (_uiState.value.selectedReason.isEmpty()) {
                    emitEvent(UserWithdrawalSurveyEvent.Error("탈퇴 사유를 선택해주세요."))
                    _uiState.update { it.copy(isLoading = false) }
                    return@launch
                }

                // 기타를 선택한 경우 추가 사유 입력이 필수
                if (_uiState.value.selectedReason.contains(WithdrawalReason.OTHER) &&
                    _uiState.value.additionalReason.trim().isEmpty()
                ) {
                    emitEvent(UserWithdrawalSurveyEvent.Error("기타를 선택한 경우 추가 사유를 입력해주세요."))
                    _uiState.update { it.copy(isLoading = false) }
                    return@launch
                }

                // 선택된 사유들을 리스트로 구성
                val reasonList = _uiState.value.selectedReason.map { reason ->
                    when (reason) {
                        WithdrawalReason.OTHER -> "기타: ${_uiState.value.additionalReason.trim()}"
                        else -> reason.displayName
                    }
                }

                // 계정 삭제 사유 전송
                accountDeletionReasonUseCase(reason = reasonList).collect { reasonResult ->
                    when (reasonResult) {
                        is DomainResult.Success -> {
                            // 회원 탈퇴 처리
                            withdrawUseCase().collect { withdrawResult ->
                                when (withdrawResult) {
                                    is DomainResult.Success -> {
                                        emitEvent(UserWithdrawalSurveyEvent.WithdrawSuccess)
                                    }

                                    is DomainResult.Exception -> {
                                        emitEvent(UserWithdrawalSurveyEvent.Error("알 수 없는 오류가 발생했습니다."))
                                    }

                                    is DomainResult.Error -> {
                                        emitEvent(
                                            UserWithdrawalSurveyEvent.Error(
                                                withdrawResult.message ?: "회원 탈퇴 처리 중 오류가 발생했습니다."
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        is DomainResult.Exception -> {
                            emitEvent(UserWithdrawalSurveyEvent.Error("알 수 없는 오류가 발생했습니다."))
                        }

                        is DomainResult.Error -> {
                            emitEvent(
                                UserWithdrawalSurveyEvent.Error(
                                    reasonResult.message ?: "탈퇴 사유 전송 중 오류가 발생했습니다."
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                emitEvent(UserWithdrawalSurveyEvent.Error("처리 중 오류가 발생했습니다: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
