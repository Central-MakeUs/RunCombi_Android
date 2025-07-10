package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class TokenReissueRequest(
    val refreshToken: String
)
