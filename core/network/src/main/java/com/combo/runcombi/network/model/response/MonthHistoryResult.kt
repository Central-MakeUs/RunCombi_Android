package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MonthHistoryResult(
    val avgCal: Int,
    val avgDistance: Double,
    val monthData: List<MonthData>,
    val mostRunStyle: String
)