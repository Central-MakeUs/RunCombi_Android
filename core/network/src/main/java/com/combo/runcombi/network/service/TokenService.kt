package com.combo.runcombi.network.service

import com.combo.runcombi.network.model.response.TokenReissue
import retrofit2.Response
import retrofit2.http.GET

interface TokenService {
    @GET("auth/refresh")
    suspend fun requestTokenReissue(): Response<TokenReissue>
}