package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DayHistoryResult(
    val regDate: String,
    val runDistance: Double,
    val runId: Int,
    val runImageUrl: String,
    val runTime: Int
)