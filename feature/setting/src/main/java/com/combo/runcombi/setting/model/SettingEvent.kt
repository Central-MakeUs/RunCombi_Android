package com.combo.runcombi.setting.model

sealed interface SettingEvent {
    data object LogoutSuccess : SettingEvent
    data object WithdrawSuccess : SettingEvent
    data class Error(val message: String) : SettingEvent
}