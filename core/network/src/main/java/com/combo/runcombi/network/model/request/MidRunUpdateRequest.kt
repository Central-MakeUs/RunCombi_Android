package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class MidRunUpdateRequest(
    val runId: Int,
    val runTime: Int,
    val runDistance: Double
)
