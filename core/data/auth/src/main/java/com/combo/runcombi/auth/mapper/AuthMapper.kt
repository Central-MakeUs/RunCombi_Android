package com.combo.runcombi.auth.mapper

import com.combo.runcombi.auth.model.KakaoLogin
import com.combo.runcombi.network.model.response.LoginResponse


fun LoginResponse.toDomainModel(): KakaoLogin {
    return with(result) {
        KakaoLogin(
            accessToken = accessToken.orEmpty(),
            refreshToken = refreshToken.orEmpty(),
            memberId = memberId.orEmpty(),
            isFinishedRegister = finishRegister == "Y"
        )
    }
}

