package com.combo.runcombi.pet.usecase

import com.combo.runcombi.pet.model.Pet
import com.combo.runcombi.pet.repository.PetRepository
import com.combo.runcombi.common.DomainResult
import javax.inject.Inject

class GetPetListUseCase @Inject constructor(
    private val petRepository: PetRepository,
) {
    fun invoke(): List<Pet> {
        return when (val response = petRepository.getPetList()) {
            is DomainResult.Success -> {
                response.data
            }

            is DomainResult.Error, is DomainResult.Exception -> listOf()
        }
    }
}
