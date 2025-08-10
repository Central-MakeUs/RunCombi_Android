package com.combo.runcombi.network.service

import com.combo.runcombi.network.model.request.AnnouncementDetailRequest
import com.combo.runcombi.network.model.request.CheckVersionRequest
import com.combo.runcombi.network.model.request.KakaoLoginRequest
import com.combo.runcombi.network.model.request.LeaveReasonRequest
import com.combo.runcombi.network.model.request.SuggestionRequest
import com.combo.runcombi.network.model.response.AnnouncementDetailResponse
import com.combo.runcombi.network.model.response.AnnouncementResponse
import com.combo.runcombi.network.model.response.BaseResponse
import com.combo.runcombi.network.model.response.CheckVersionResponse
import com.combo.runcombi.network.model.response.DefaultResponse
import com.combo.runcombi.network.model.response.DeleteDataResponse
import com.combo.runcombi.network.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SettingService {
    @POST("/api/member/getDeleteData")
    suspend fun getDeleteData(
    ): Response<DeleteDataResponse>

    @POST("/api/member/leaveReason")
    suspend fun leaveReason(
        @Body request: LeaveReasonRequest,
    ): Response<DefaultResponse>

    @POST("/api/member/suggestion")
    suspend fun suggestion(
        @Body request: SuggestionRequest,
    ): Response<BaseResponse>

    @POST("/version/check")
    suspend fun checkVersion(
        @Body request: CheckVersionRequest,
    ): Response<CheckVersionResponse>

    @POST("/announcement/getAnnouncementList")
    suspend fun getAnnouncementList(): Response<AnnouncementResponse>

    @POST("/announcement/getAnnouncementDetail")
    suspend fun getAnnouncementDetail(
        @Body request: AnnouncementDetailRequest,
    ): Response<AnnouncementDetailResponse>

}