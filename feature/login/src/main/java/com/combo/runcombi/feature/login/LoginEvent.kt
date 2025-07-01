package com.combo.runcombi.feature.login

sealed interface LoginEvent {
    data object Error : LoginEvent
    data object Success : LoginEvent
}