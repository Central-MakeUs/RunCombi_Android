package com.combo.runcombi.history.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.ExerciseRating
import com.combo.runcombi.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetRunEvaluatingUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
) {
    operator fun invoke(runId: Int, rating: ExerciseRating): Flow<DomainResult<Unit>> = flow {
        emit(historyRepository.setRunEvaluating(runId, rating))
    }
} 