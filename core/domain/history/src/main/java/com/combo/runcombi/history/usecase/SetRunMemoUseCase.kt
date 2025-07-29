package com.combo.runcombi.history.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetRunMemoUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
) {
    operator fun invoke(runId: Int, memo: String): Flow<DomainResult<Unit>> = flow {
        emit(historyRepository.setRunMemo(runId, memo))
    }
} 