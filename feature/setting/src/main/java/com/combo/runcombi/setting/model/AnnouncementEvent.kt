package com.combo.runcombi.setting.model

sealed class AnnouncementEvent {
    data class Error(val errorMessage: String) : AnnouncementEvent()
} 