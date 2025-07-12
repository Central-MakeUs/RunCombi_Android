package com.combo.runcombi.domain.user.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.User
import com.combo.runcombi.domain.user.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    fun invoke(): User? {
        return when (val response = userRepository.getUserInfo()) {
            is DomainResult.Success -> {
                response.data
            }

            is DomainResult.Error, is DomainResult.Exception -> null
        }
    }
}
