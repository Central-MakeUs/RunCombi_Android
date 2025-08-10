package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementDetailResult(
    val announcementId: Int?,
    val announcementImageUrl: String?,
    val announcementType: String?,
    val code: String?,
    val content: String?,
    val endDate: String?,
    val eventApplyUrl: String?,
    val regDate: String?,
    val startDate: String?,
    val title: String?,
)