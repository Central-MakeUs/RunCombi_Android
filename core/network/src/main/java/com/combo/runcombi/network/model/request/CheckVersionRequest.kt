package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CheckVersionRequest(
    val os: String,
    val version: String
)