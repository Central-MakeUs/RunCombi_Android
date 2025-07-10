package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse(
    val code: String = "",
    val isSuccess: Boolean = false,
    val message: String = "",
)