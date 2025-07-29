package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SetRunEvaluatingRequest(
    val runId: Int,
    val runEvaluating: String,
)
