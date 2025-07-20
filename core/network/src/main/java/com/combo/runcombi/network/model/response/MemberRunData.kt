package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MemberRunData(
    val runId: Int,
    val memberCal: Int,
    val runTime: Int,
    val runDistance: Double,
    val runEvaluating: String,
    val memo: String,
)
