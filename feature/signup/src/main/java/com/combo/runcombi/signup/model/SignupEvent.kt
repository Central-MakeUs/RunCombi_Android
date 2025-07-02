package com.combo.runcombi.signup

sealed interface SignupEvent {
    data object Error : SignupEvent
    data object Success : SignupEvent
}