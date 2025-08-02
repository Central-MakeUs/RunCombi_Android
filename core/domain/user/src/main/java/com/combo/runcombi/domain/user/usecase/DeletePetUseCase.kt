package com.combo.runcombi.domain.user.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeletePetUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        petId: Int,
    ): Flow<DomainResult<Unit>> = flow {
        emit(
            userRepository.deletePet(petId)
        )
    }
} 