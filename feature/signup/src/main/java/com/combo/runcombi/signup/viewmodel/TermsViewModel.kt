package com.combo.runcombi.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.usecase.AgreeTermsUseCase
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.signup.model.TermsEvent
import com.combo.runcombi.signup.model.TermsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    val agreeTermsUseCase: AgreeTermsUseCase,
    val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TermsUiState())
    val uiState: StateFlow<TermsUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<TermsEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun setAgreeTerms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            agreeTermsUseCase(
                listOf(
                    "TERMS_OF_SERVICE",
                    "PRIVACY_POLICY",
                    "LOCATION_SERVICE_AGREEMENT"
                )
            ).collectLatest {
                _uiState.update { it.copy(isLoading = false) }
                when (it) {
                    is DomainResult.Success -> {
                        _eventFlow.emit(TermsEvent.Success)
                    }

                    else -> {
                        _eventFlow.emit(
                            TermsEvent.Error(
                                errorMessage = "네트워크 에러가 발생했습니다."
                            )
                        )
                    }
                }
            }
        }
    }

    fun checkUserStatus() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getUserInfoUseCase().collectLatest { result ->
                _uiState.update { it.copy(isLoading = false) }
                when (result) {
                    is DomainResult.Success -> {
                        val userInfo = result.data
                        val userStatus = userInfo.userStatus

                        /// TODO: userStatus로 상태 체크
                        _eventFlow.emit(TermsEvent.NavigateNext)
                    }

                    else -> {
                        _eventFlow.emit(
                            TermsEvent.Error(
                                errorMessage = "네트워크 에러가 발생했습니다."
                            )
                        )
                    }
                }
            }
        }
    }

    fun setAllChecked(value: Boolean) {
        _uiState.update {
            it.copy(
                uiModel = it.uiModel.copy(
                    termsChecked = value,
                    privacyChecked = value,
                    locationChecked = value
                )
            )
        }
    }

    fun updateTermsChecked(value: Boolean) {
        _uiState.update { it.copy(uiModel = it.uiModel.copy(termsChecked = value)) }
    }

    fun updatePrivacyChecked(value: Boolean) {
        _uiState.update { it.copy(uiModel = it.uiModel.copy(privacyChecked = value)) }
    }

    fun updateLocationChecked(value: Boolean) {
        _uiState.update { it.copy(uiModel = it.uiModel.copy(locationChecked = value)) }
    }
}