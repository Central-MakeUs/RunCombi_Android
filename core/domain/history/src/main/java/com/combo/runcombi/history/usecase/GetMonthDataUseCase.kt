package com.combo.runcombi.history.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.MonthHistory
import com.combo.runcombi.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMonthDataUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
) {
    operator fun invoke(year: Int, month: Int): Flow<DomainResult<MonthHistory>> =
        flow {
            emit(historyRepository.getMonthData(year, month))
        }
} 