package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HistoryResponse(
    val result: HistoryResult
): BaseResponse()