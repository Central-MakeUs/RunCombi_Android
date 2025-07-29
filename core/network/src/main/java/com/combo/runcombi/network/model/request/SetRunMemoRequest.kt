package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SetRunMemoRequest(
    val runId: Int,
    val memo: String,
)
