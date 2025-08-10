package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementResult(
    val announcementId: Int?,
    val announcementType: String?,
    val endDate: String?,
    val isRead: String?,
    val regDate: String?,
    val startDate: String?,
    val title: String?
)