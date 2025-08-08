package com.combo.runcombi.history.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteRunDataUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
) {
    operator fun invoke(runId: Int): Flow<DomainResult<Unit>> = flow {
        emit(historyRepository.deleteRunData(runId = runId))
    }
} 