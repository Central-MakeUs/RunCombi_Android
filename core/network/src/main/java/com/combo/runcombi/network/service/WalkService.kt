package com.combo.runcombi.network.service

import com.combo.runcombi.network.model.request.KakaoLoginRequest
import com.combo.runcombi.network.model.request.StartRunRequest
import com.combo.runcombi.network.model.response.DefaultResponse
import com.combo.runcombi.network.model.response.LoginResponse
import com.combo.runcombi.network.model.response.StartRunResponse
import com.combo.runcombi.network.model.response.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface WalkService {

    @POST("api/run/startRun")
    suspend fun requestStartRun(
        @Body request: StartRunRequest,
    ): Response<StartRunResponse>

    @Multipart
    @POST("api/run/endRun")
    suspend fun requestEndRun(
        @Part("memberRunData") memberRunData: RequestBody,
        @Part("petRunData") petRunData: RequestBody,
        @Part routeImage: MultipartBody.Part?,
        @Part runImage: MultipartBody.Part?,
    ): Response<DefaultResponse>

}