package com.combo.runcombi.history.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateRunDataUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
) {
    operator fun invoke(
        runId: Int,
        regDate: String,
        memberRunStyle: String,
        runTime: Int,
        runDistance: Double,
    ): Flow<DomainResult<Unit>> = flow {
        emit(
            historyRepository.updateRunDetail(
                runId = runId,
                regDate = regDate,
                memberRunStyle = memberRunStyle,
                runTime = runTime,
                runDistance = runDistance
            )
        )
    }
} 