package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResult(
    val accessToken: String,
    val refreshToken: String,
    val email: String,
    val finishRegister: String,
    val memberId: String,
)