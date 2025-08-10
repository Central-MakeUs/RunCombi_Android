package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CheckVersionResponse(
    val result: CheckVersionResult
): BaseResponse()