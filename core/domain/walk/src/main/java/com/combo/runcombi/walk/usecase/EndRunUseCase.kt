package com.combo.runcombi.walk.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.walk.repository.WalkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class EndRunUseCase @Inject constructor(
    private val walkRepository: WalkRepository,
) {
    suspend operator fun invoke(
        runId: Int,
        runTime: Int,
        runDistance: Double,
        petList: List<Int>,
        routeImage: File?,
    ): Flow<DomainResult<Unit>> = flow {
        emit(
            walkRepository.endRun(
                runId,
                runTime,
                runDistance,
                petList,
                routeImage,
            )
        )
    }
} 