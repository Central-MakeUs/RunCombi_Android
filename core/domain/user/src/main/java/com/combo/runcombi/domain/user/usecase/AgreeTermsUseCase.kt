package com.combo.runcombi.domain.user.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AgreeTermsUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(agreeTerms: List<String>): Flow<DomainResult<Unit>> = flow {
        emit(userRepository.setUserTerms(agreeTerms))
    }
} 