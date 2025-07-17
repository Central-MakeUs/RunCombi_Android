package com.combo.runcombi.signup.model

sealed interface TermsEvent {
    data object Success : TermsEvent
    data object NavigateNext : TermsEvent
    data class Error(val errorMessage: String) : TermsEvent
}