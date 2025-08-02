package com.combo.runcombi.domain.user.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetUserStatusUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): MemberStatus = runBlocking(Dispatchers.IO) {
        (userRepository.getUserInfo() as? DomainResult.Success)?.data?.memberStatus
            ?: MemberStatus.PENDING_AGREE
    }
}
