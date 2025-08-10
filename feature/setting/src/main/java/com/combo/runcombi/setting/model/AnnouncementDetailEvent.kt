package com.combo.runcombi.setting.model

sealed class AnnouncementDetailEvent {
    data class Error(val errorMessage: String) : AnnouncementDetailEvent()
} 