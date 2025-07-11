package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val loginResult: LoginResult
): BaseResponse()