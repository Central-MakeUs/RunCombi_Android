package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse(
    val result: String
): BaseResponse()