package com.combo.runcombi.signup.model

sealed interface SignupEvent {
    data class Error(val errorMessage: String) : SignupEvent
    data object NavigateNext : SignupEvent
    data object Success : SignupEvent
}