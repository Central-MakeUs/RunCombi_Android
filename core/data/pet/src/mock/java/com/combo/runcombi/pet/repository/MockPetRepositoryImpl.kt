package com.combo.runcombi.pet.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.pet.model.Pet
import com.combo.runcombi.pet.model.WalkStyle
import javax.inject.Inject

class MockPetRepositoryImpl @Inject constructor() : PetRepository {
    override fun getPetList(): DomainResult<List<Pet>> {
        return DomainResult.Success(
            listOf(
                Pet(
                    name = "모카",
                    weight = 5.0,
                    age = 3,
                    profileImageUrl = null,
                    walkStyle = WalkStyle.RELAXED
                ),
                Pet(
                    name = "초코",
                    weight = 7.2,
                    age = 5,
                    profileImageUrl = null,
                    walkStyle = WalkStyle.SLOW
                )
            )
        )
    }

}
