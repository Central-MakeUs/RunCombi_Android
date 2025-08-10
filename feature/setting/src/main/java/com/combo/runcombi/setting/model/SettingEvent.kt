package com.combo.runcombi.setting.model

sealed interface SettingEvent {
    data object LogoutSuccess : SettingEvent
    data class Error(val message: String) : SettingEvent
}