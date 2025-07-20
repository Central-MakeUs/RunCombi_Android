package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class StartRunRequest(
    val memberRunStyle: String,
    val petList: List<Int>
)