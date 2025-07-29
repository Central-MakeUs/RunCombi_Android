package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class GetMonthDataRequest(
    val year: Int,
    val month: Int,
)
