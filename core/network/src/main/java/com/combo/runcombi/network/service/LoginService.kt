package com.combo.runcombi.network.service

import com.combo.runcombi.network.model.request.KakaoLoginRequest
import com.combo.runcombi.network.model.response.LoginResponse
import com.combo.runcombi.network.model.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("auth/kakao/login")
    suspend fun requestKakaoLogin(
        @Body request: KakaoLoginRequest,
    ): Response<LoginResponse>

}