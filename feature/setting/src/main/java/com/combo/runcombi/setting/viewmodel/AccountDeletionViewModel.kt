package com.combo.runcombi.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.auth.usecase.WithdrawUseCase
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.model.AccountDeletionEvent
import com.combo.runcombi.setting.model.SettingEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDeletionViewModel @Inject constructor(
    private val withdrawUseCase: WithdrawUseCase,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<AccountDeletionEvent>()
    val eventFlow: SharedFlow<AccountDeletionEvent> = _eventFlow.asSharedFlow()

    private fun emitEvent(event: AccountDeletionEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun tryWithDraw() {
        viewModelScope.launch {
            _isLoading.value = true
            withdrawUseCase().collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        emitEvent(AccountDeletionEvent.WithdrawSuccess)
                    }

                    is DomainResult.Exception -> {
                        emitEvent(AccountDeletionEvent.Error("알 수 없는 오류가 발생했습니다."))
                    }

                    is DomainResult.Error -> {
                        emitEvent(AccountDeletionEvent.Error(result.message ?: "오류가 발생했습니다."))
                    }
                }
                _isLoading.value = false
            }
        }
    }

}