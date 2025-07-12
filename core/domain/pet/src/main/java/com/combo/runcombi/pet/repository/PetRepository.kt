package com.combo.runcombi.pet.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.pet.model.Pet

interface PetRepository {
    fun getPetList(): DomainResult<List<Pet>>
}
