package com.combo.runcombi.setting.model


sealed interface AccountDeletionEvent {
    data object WithdrawSuccess : AccountDeletionEvent
    data class Error(val message: String) : AccountDeletionEvent
}