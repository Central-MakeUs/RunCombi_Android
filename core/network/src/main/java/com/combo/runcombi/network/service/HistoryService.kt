package com.combo.runcombi.network.service

import com.combo.runcombi.network.model.request.AddRunRequest
import com.combo.runcombi.network.model.request.DeleteRunRequest
import com.combo.runcombi.network.model.request.GetDayDataRequest
import com.combo.runcombi.network.model.request.GetMonthDataRequest
import com.combo.runcombi.network.model.request.GetRunDataRequest
import com.combo.runcombi.network.model.request.SetRunEvaluatingRequest
import com.combo.runcombi.network.model.request.SetRunMemoRequest
import com.combo.runcombi.network.model.request.UpdateRunDetailRequest
import com.combo.runcombi.network.model.response.DayHistoryResponse
import com.combo.runcombi.network.model.response.DefaultResponse
import com.combo.runcombi.network.model.response.HistoryResponse
import com.combo.runcombi.network.model.response.MonthHistoryResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface HistoryService {
    @POST("api/calender/getRunData")
    suspend fun getRunData(
        @Body request: GetRunDataRequest,
    ): Response<HistoryResponse>

    @POST("api/calender/getMonthData")
    suspend fun getMonthData(
        @Body request: GetMonthDataRequest,
    ): Response<MonthHistoryResponse>

    @POST("api/calender/getDayData")
    suspend fun getDayData(
        @Body request: GetDayDataRequest,
    ): Response<DayHistoryResponse>

    @POST("api/calender/setRunEvaluating")
    suspend fun setRunEvaluating(
        @Body request: SetRunEvaluatingRequest,
    ): Response<DefaultResponse>

    @POST("api/calender/updateMemo")
    suspend fun setRunMemo(
        @Body request: SetRunMemoRequest,
    ): Response<DefaultResponse>

    @Multipart
    @POST("api/calender/setRunImage")
    suspend fun setRunImage(
        @Part runId: MultipartBody.Part?,
        @Part runImage: MultipartBody.Part?,
    ): Response<DefaultResponse>

    @POST("api/calender/deleteRun")
    suspend fun deleteRun(
        @Body request: DeleteRunRequest,
    ): Response<DefaultResponse>

    @POST("api/calender/addRun")
    suspend fun addRun(
        @Body request: AddRunRequest,
    ): Response<DefaultResponse>

    @POST("api/calender/updateRunDetail")
    suspend fun updateRunDetail(
        @Body request: UpdateRunDetailRequest,
    ): Response<DefaultResponse>
}