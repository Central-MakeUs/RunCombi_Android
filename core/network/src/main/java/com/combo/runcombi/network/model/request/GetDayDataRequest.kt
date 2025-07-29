package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class GetDayDataRequest(
    val year: Int,
    val month: Int,
    val day: Int,
)
