package com.combo.runcombi.auth.model

data class KakaoLogin(
    val accessToken: String,
    val refreshToken: String,
    val isFinishedRegister: Boolean,
    val memberId: String,
)
