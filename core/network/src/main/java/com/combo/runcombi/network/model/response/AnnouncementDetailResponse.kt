package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementDetailResponse(
    val result: AnnouncementDetailResult,
) : BaseResponse()