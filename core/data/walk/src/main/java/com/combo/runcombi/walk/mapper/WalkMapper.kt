package com.combo.runcombi.walk.mapper

import com.combo.runcombi.network.model.response.PetCal
import com.combo.runcombi.network.model.response.StartRunResponse
import com.combo.runcombi.network.model.response.StartRunResult
import com.combo.runcombi.walk.model.StartRunData
import com.combo.runcombi.walk.model.WalkPet

fun StartRunResponse.toDomainModel(): StartRunData {
    return StartRunData(
        runId = result.runId,
        isFirstRun = result.isFirstRun,
        nthRun = result.nthRun
    )
}

fun WalkPet.toDataModel(): PetCal {
    return PetCal(
        petId = petId,
        petCal = petCal
    )
}

