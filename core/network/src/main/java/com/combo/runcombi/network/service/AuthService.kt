package com.combo.runcombi.network.service

import com.combo.runcombi.network.model.request.KakaoLoginRequest
import com.combo.runcombi.network.model.response.DefaultResponse
import com.combo.runcombi.network.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/kakao/login")
    suspend fun requestKakaoLogin(
        @Body request: KakaoLoginRequest,
    ): Response<LoginResponse>

    @POST("api/member/deleteAccount")
    suspend fun requestWithdraw(
    ): Response<DefaultResponse>

}