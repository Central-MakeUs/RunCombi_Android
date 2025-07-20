package com.combo.runcombi.walk.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.walk.model.WalkPet
import java.io.File


interface WalkRepository {
    suspend fun startRun(petList: List<Int>, memberRunStyle: String): DomainResult<Int>

    suspend fun endRun(
        runId: Int,
        memberCal: Int,
        runTime: Int,
        runDistance: Double,
        memo: String,
        runEvaluating: String,
        petList: List<WalkPet>,
        routeImage: File?,
        runImage: File?,
    ): DomainResult<Unit>

}