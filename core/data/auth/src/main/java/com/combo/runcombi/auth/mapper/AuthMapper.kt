package com.combo.runcombi.auth.mapper

import com.combo.runcombi.auth.model.KakaoLogin
import com.combo.runcombi.network.model.response.LoginResponse


fun LoginResponse.toModel(): KakaoLogin {
    return KakaoLogin(
        accessToken = loginResult.accessToken,
        refreshToken = loginResult.refreshToken,
        memberId = loginResult.memberId,
        isFinishedRegister = loginResult.finishRegister == "Y"
    )
}
