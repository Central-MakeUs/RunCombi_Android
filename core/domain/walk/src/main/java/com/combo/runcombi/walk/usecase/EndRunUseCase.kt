package com.combo.runcombi.walk.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.walk.model.WalkPet
import com.combo.runcombi.walk.repository.WalkRepository
import java.io.File
import javax.inject.Inject

class EndRunUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(
        runId: Int,
        memberCal: Int,
        runTime: Int,
        runDistance: Double,
        memo: String,
        runEvaluating: String,
        petList: List<WalkPet>,
        routeImage: File?,
        runImage: File?,
    ): DomainResult<Unit> {
        return walkRepository.endRun(
            runId, memberCal, runTime, runDistance, memo, runEvaluating, petList, routeImage, runImage
        )
    }
} 