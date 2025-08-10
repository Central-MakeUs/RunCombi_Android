package com.combo.runcombi.setting.model

data class AnnouncementUiState(
    val eventList: List<Announcement> = emptyList(),
    val noticeList: List<Announcement> = emptyList(),
    val isLoading: Boolean = false,
)