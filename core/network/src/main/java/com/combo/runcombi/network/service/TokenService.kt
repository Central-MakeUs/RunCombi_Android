package com.combo.runcombi.network.service

import com.combo.runcombi.network.model.request.TokenReissueRequest
import com.combo.runcombi.network.model.response.TokenReissue
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {
    @POST("/api/auth/token:reissue")
    suspend fun requestTokenReissue(
        @Body request: TokenReissueRequest,
    ): Response<TokenReissue>
}