package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DayHistoryResponse(
    val result: List<DayHistoryResult>
) : BaseResponse()