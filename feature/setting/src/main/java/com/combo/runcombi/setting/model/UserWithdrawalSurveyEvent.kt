package com.combo.runcombi.setting.model


sealed interface UserWithdrawalSurveyEvent {
    data object WithdrawSuccess : UserWithdrawalSurveyEvent
    data class Error(val message: String) : UserWithdrawalSurveyEvent
}