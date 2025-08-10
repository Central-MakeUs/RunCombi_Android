package com.combo.runcombi.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.model.UserWithdrawalInfoEvent
import com.combo.runcombi.setting.model.UserWithdrawalInfoUiState
import com.combo.runcombi.setting.usecase.GetDeleteDataUseCase
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
class UserWithdrawalInfoViewModel @Inject constructor(
    private val getDeleteDataUseCase: GetDeleteDataUseCase,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<UserWithdrawalInfoEvent>()
    val eventFlow: SharedFlow<UserWithdrawalInfoEvent> = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(UserWithdrawalInfoUiState())
    val uiState: StateFlow<UserWithdrawalInfoUiState> = _uiState.asStateFlow()

    init {
        getDeleteDate()
    }

    private fun getDeleteDate() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getDeleteDataUseCase().collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        with(result.data) {
                            _uiState.update {
                                it.copy(
                                    petList = resultPetName,
                                    imageCount = resultRunImage,
                                    runCount = resultRun
                                )
                            }
                        }
                    }

                    else -> {
                        _eventFlow.emit(UserWithdrawalInfoEvent.Error(message = "알 수 없는 에러가 발생했습니다."))
                    }
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

}
