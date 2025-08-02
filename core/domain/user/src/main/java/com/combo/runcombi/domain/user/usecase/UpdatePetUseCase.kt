package com.combo.runcombi.domain.user.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class UpdatePetUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        petDetail: Pet,
        petImage: File?,
    ): Flow<DomainResult<Unit>> = flow {
        emit(
            userRepository.updatePet(petDetail, petImage)
        )
    }
} 