package com.combo.runcombi.walk.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.walk.repository.WalkRepository
import javax.inject.Inject

class StartRunUseCase @Inject constructor(
    private val walkRepository: WalkRepository
) {
    suspend operator fun invoke(petList: List<Int>, memberRunStyle: String): DomainResult<Int> {
        return walkRepository.startRun(petList, memberRunStyle)
    }
} 