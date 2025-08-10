package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LeaveReasonRequest(
    val reason: List<String>
)