package com.combo.runcombi.auth.mapper

import com.combo.runcombi.auth.model.KakaoLogin
import com.combo.runcombi.network.model.response.TokenResponse


fun TokenResponse.toModel(): KakaoLogin {
    return KakaoLogin(
        accessToken = result.accessToken,
        refreshToken = result.refreshToken,
        nickname = "",
    )
}
