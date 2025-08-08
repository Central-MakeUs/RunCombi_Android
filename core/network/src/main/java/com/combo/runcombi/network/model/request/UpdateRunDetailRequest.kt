package com.combo.runcombi.network.model.request

import com.combo.runcombi.network.model.response.PetId
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRunDetailRequest(
    val memberRunStyle: String,
    val runTime: Int,
    val runDistance: Double,
    val regDate: String,
    val runId: Int,
)