package com.combo.runcombi.setting.model

data class AnnouncementDetail(
    val announcementId: Int,
    val announcementImageUrl: String,
    val announcementType: String,
    val code: String,
    val content: String,
    val endDate: String,
    val eventApplyUrl: String,
    val regDate: String,
    val startDate: String,
    val title: String,
)