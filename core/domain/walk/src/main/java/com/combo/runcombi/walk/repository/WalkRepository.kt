package com.combo.runcombi.walk.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.walk.model.StartRunData
import java.io.File


interface WalkRepository {
    suspend fun startRun(petList: List<Int>, memberRunStyle: String): DomainResult<StartRunData>

    suspend fun endRun(
        runId: Int,
        runTime: Int,
        runDistance: Double,
        petList: List<Int>,
        routeImage: File?,
    ): DomainResult<Unit>

}