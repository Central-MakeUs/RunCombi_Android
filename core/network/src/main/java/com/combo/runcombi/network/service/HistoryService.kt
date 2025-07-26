package com.combo.runcombi.network.service

import com.combo.runcombi.network.model.request.GetRunDataRequest
import com.combo.runcombi.network.model.response.DayHistoryResponse
import com.combo.runcombi.network.model.response.DefaultResponse
import com.combo.runcombi.network.model.response.HistoryResponse
import com.combo.runcombi.network.model.response.MonthHistoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HistoryService {
    @POST("api/calender/getRunData")
    suspend fun getRunData(
        @Body request: GetRunDataRequest,
    ): Response<HistoryResponse>

    @FormUrlEncoded
    @POST("api/calender/getMonthData")
    suspend fun getMonthData(
        @Field("year") year: Int,
        @Field("month") month: Int,
    ): Response<MonthHistoryResponse>

    @POST("api/calender/getDayData")
    suspend fun getDayData(
    ): Response<DayHistoryResponse>

    @FormUrlEncoded
    @POST("api/calender/setRunEvaluating")
    suspend fun setRunEvaluating(
        @Field("runId") runId: Int,
        @Field("runEvaluating") runEvaluating: String,
    ): Response<DefaultResponse>
}