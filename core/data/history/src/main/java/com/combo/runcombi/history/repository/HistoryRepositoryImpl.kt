package com.combo.runcombi.history.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.common.convert
import com.combo.runcombi.common.handleResult
import com.combo.runcombi.history.mapper.toDomainModel
import com.combo.runcombi.history.model.DayHistory
import com.combo.runcombi.history.model.ExerciseRating
import com.combo.runcombi.history.model.MonthHistory
import com.combo.runcombi.history.model.RunData
import com.combo.runcombi.network.model.request.GetDayDataRequest
import com.combo.runcombi.network.model.request.GetMonthDataRequest
import com.combo.runcombi.network.model.request.GetRunDataRequest
import com.combo.runcombi.network.model.request.SetRunEvaluatingRequest
import com.combo.runcombi.network.model.request.SetRunMemoRequest
import com.combo.runcombi.network.service.HistoryService
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
                    runId = runId,
                    runEvaluating = rating.name
                )
            )
        }.convert {}

    override suspend fun setRunMemo(runId: Int, memo: String): DomainResult<Unit> = handleResult {
        historyService.setRunMemo(
            SetRunMemoRequest(
                runId = runId,
                memo = memo
            )
        )
    }.convert {}

}