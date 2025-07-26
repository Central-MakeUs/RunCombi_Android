package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MonthData(
    val date: String,
    val runId: List<Int>
)