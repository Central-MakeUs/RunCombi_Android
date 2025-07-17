package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val result: UserInfoResult,
) : BaseResponse()