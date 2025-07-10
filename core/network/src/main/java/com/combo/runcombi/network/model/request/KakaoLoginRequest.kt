package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginRequest(
    val kakaoAccessToken: String
)