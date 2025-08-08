package com.combo.runcombi.history.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.common.convert
import com.combo.runcombi.common.handleResult
import com.combo.runcombi.history.mapper.toDomainModel
import com.combo.runcombi.history.model.DayHistory
import com.combo.runcombi.history.model.ExerciseRating
import com.combo.runcombi.history.model.MonthHistory
import com.combo.runcombi.history.model.RunData
import com.combo.runcombi.network.model.request.AddRunRequest
import com.combo.runcombi.network.model.request.DeleteRunRequest
import com.combo.runcombi.network.model.request.GetDayDataRequest
import com.combo.runcombi.network.model.request.GetMonthDataRequest
import com.combo.runcombi.network.model.request.GetRunDataRequest
import com.combo.runcombi.network.model.request.SetRunEvaluatingRequest
import com.combo.runcombi.network.model.request.SetRunMemoRequest
import com.combo.runcombi.network.model.request.UpdateRunDetailRequest
import com.combo.runcombi.network.model.response.PetId
import com.combo.runcombi.network.service.HistoryService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(private val historyService: HistoryService) :
    HistoryRepository {

    override suspend fun getRunData(runId: Int): DomainResult<RunData> = handleResult {
        historyService.getRunData(GetRunDataRequest(runId))
    }.convert {
        it.toDomainModel()
    }

    override suspend fun getMonthData(year: Int, month: Int): DomainResult<MonthHistory> =
        handleResult {
            historyService.getMonthData(GetMonthDataRequest(year = year, month = month))
        }.convert {
            it.toDomainModel()
        }

    override suspend fun getDayData(
        year: Int,
        month: Int,
        day: Int,
    ): DomainResult<List<DayHistory>> = handleResult {
        historyService.getDayData(GetDayDataRequest(year = year, month = month, day = day))
    }.convert {
        it.toDomainModel()
    }

    override suspend fun setRunEvaluating(runId: Int, rating: ExerciseRating): DomainResult<Unit> =
        handleResult {
            historyService.setRunEvaluating(
                SetRunEvaluatingRequest(
                    runId = runId, runEvaluating = rating.name
                )
            )
        }.convert {}

    override suspend fun setRunMemo(runId: Int, memo: String): DomainResult<Unit> = handleResult {
        historyService.setRunMemo(
            SetRunMemoRequest(
                runId = runId, memo = memo
            )
        )
    }.convert {}

    override suspend fun setRunImage(runId: Int, runImage: File): DomainResult<Unit> =
        handleResult {
            val runIdPart = MultipartBody.Part.createFormData(
                "runId", null, runId.toString().toRequestBody("application/json".toMediaType())
            )

            val runImagePart = MultipartBody.Part.createFormData(
                "runImage", runImage.name, runImage.asRequestBody("image/*".toMediaTypeOrNull())
            )
            historyService.setRunImage(
                runIdPart, runImagePart
            )
        }.convert { }

    override suspend fun deleteRunData(runId: Int): DomainResult<Unit> =
        handleResult {
            historyService.deleteRun(DeleteRunRequest(runId = runId))
        }.convert { }

    override suspend fun updateRunDetail(
        runId: Int,
        regDate: String,
        memberRunStyle: String,
        runTime: Int,
        runDistance: Double,
    ): DomainResult<Unit> = handleResult {
        historyService.updateRunDetail(
            UpdateRunDetailRequest(
                memberRunStyle = memberRunStyle,
                runTime = runTime,
                runDistance = runDistance,
                regDate = regDate,
                runId = runId
            )
        )
    }.convert { }

    override suspend fun addRunData(
        regDate: String,
        memberRunStyle: String,
        runTime: Int,
        runDistance: Double,
        petCalList: List<Int>,
    ): DomainResult<Unit> = handleResult {
        historyService.addRun(
            AddRunRequest(
                memberRunStyle = memberRunStyle,
                runTime = runTime,
                runDistance = runDistance,
                regDate = regDate,
                petCalList = petCalList.map {
                    PetId(petId = it)
                },
            )
        )
    }.convert { }

}