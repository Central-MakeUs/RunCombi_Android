package com.combo.runcombi.history.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddRunDataUseCase @Inject constructor(
    private val historyRepository: HistoryRepository,
) {
    operator fun invoke(
        regDate: String,
        memberRunStyle: String,
        runTime: Int,
        runDistance: Double,
        petCalList: List<Int>,
    ): Flow<DomainResult<Unit>> = flow {
        emit(
            historyRepository.addRunData(
                regDate = regDate,
                memberRunStyle = memberRunStyle,
                runTime = runTime,
                runDistance = runDistance,
                petCalList = petCalList
            )
        )
    }
} 