package com.combo.runcombi.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.auth.usecase.LogoutUseCase
import com.combo.runcombi.auth.usecase.WithdrawUseCase
import com.combo.runcombi.common.DomainResult
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
class SettingViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<SettingEvent>()
    val eventFlow: SharedFlow<SettingEvent> = _eventFlow.asSharedFlow()

    private fun emitEvent(event: SettingEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun tryLogout() {
        viewModelScope.launch {
            _isLoading.value = true
            logoutUseCase().collect {
                emitEvent(SettingEvent.LogoutSuccess)
                _isLoading.value = false
            }
        }
    }
}