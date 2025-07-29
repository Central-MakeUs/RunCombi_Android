package com.combo.runcombi.history.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.DayHistory
import com.combo.runcombi.history.model.RunData
import com.combo.runcombi.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDayDataUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
) {
    operator fun invoke(year: Int, month: Int, day: Int): Flow<DomainResult<List<DayHistory>>> =
        flow {
            emit(historyRepository.getDayData(year, month, day))
        }
} 