package com.combo.runcombi.history.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.RunData
import com.combo.runcombi.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRunDataUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
) {
    operator fun invoke(runId: Int): Flow<DomainResult<RunData>> = flow {
        emit(historyRepository.getRunData(runId))
    }
} 