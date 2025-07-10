package com.combo.runcombi.auth.model

data class KakaoLogin(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String?,
)
