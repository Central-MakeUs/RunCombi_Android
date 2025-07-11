package com.combo.runcombi.feature.login

sealed interface LoginEvent {
    data object Error : LoginEvent
    data class Success(val isFinishedRegister: Boolean) : LoginEvent
}