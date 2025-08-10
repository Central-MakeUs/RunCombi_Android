package com.combo.runcombi.setting.model


sealed interface UserWithdrawalInfoEvent {
    data class Error(val message: String) : UserWithdrawalInfoEvent
}