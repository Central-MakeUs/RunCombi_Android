package com.combo.runcombi.walk.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.walk.repository.WalkRepository
import javax.inject.Inject

class MidRunUpdateUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(
        runId: Int,
        runTime: Int,
        runDistance: Double,
    ): DomainResult<Unit> {
        return walkRepository.midRunUpdate(runId, runTime, runDistance)
    }
}
