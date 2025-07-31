package com.combo.runcombi.history.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class SetRunImageUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
) {
    operator fun invoke(runId: Int, runImage: File): Flow<DomainResult<Unit>> = flow {
        emit(historyRepository.setRunImage(runId, runImage))
    }
} 