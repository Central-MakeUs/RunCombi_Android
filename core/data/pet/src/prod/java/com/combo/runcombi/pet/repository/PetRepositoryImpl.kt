package com.combo.runcombi.pet.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.pet.model.Pet
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor() : PetRepository {
    override fun getPetList(): DomainResult<List<Pet>> {
        TODO("Not yet implemented")
    }
} 