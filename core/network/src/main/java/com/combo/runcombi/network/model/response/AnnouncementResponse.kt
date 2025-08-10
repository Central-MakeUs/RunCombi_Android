package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementResponse(
    val result: List<AnnouncementResult>
): BaseResponse()