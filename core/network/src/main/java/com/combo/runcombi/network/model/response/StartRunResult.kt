package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class StartRunResult(
    val runId: Int,
    val isFirstRun: String,
    val nthRun: Int,
)