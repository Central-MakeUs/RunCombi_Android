package com.combo.runcombi.history.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.DayHistory
import com.combo.runcombi.history.model.ExerciseRating
import com.combo.runcombi.history.model.MonthHistory
import com.combo.runcombi.history.model.RunData
import java.io.File


interface HistoryRepository {
    suspend fun getRunData(runId: Int): DomainResult<RunData>

    suspend fun getMonthData(year: Int, month: Int): DomainResult<MonthHistory>

    suspend fun getDayData(year: Int, month: Int, day: Int): DomainResult<List<DayHistory>>

    suspend fun setRunEvaluating(runId: Int, rating: ExerciseRating): DomainResult<Unit>

    suspend fun setRunMemo(runId: Int, memo: String): DomainResult<Unit>

    suspend fun setRunImage(runId: Int, runImage: File): DomainResult<Unit>
}
