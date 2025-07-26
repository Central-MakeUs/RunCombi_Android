package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MonthHistoryResponse(
    val result: MonthHistoryResult,
) : BaseResponse()