package com.combo.runcombi.walk.mapper

import com.combo.runcombi.network.model.response.PetCal
import com.combo.runcombi.walk.model.WalkPet


fun WalkPet.toDataModel(): PetCal {
    return PetCal(
        petId = petId,
        petCal = petCal
    )
}

