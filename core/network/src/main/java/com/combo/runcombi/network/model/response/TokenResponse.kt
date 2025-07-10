package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val result: TokenReissue,
) : BaseResponse()