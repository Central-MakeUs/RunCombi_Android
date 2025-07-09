package com.combo.runcombi.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun login(result: LoginData) {
        viewModelScope.launch {
            when (result) {
                is LoginData.Success -> _eventFlow.emit(LoginEvent.Success)
                is LoginData.Failed -> _eventFlow.emit(LoginEvent.Error)
            }
        }
    }
}
