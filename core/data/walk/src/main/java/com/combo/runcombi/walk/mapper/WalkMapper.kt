package com.combo.runcombi.walk.mapper

import com.combo.runcombi.network.model.response.PetCal
import com.combo.runcombi.network.model.response.StartRunResponse
import com.combo.runcombi.network.model.response.StartRunResult
import com.combo.runcombi.walk.model.StartRunData

fun StartRunResponse.toDomainModel(): StartRunData {
    return StartRunData(
        runId = result.runId,
        isFirstRun = result.isFirstRun,
        nthRun = result.nthRun
    )
}
