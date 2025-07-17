package com.combo.runcombi.domain.user.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class SetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        memberDetail: Member,
        petDetail: Pet,
        memberImage: File?,
        petImage: File?,
    ): Flow<DomainResult<Unit>> = flow {
        emit(
            userRepository.setUserInfo(
                memberDetail = memberDetail,
                petDetail = petDetail,
                memberImage = memberImage,
                petImage = petImage
            )
        )
    }
} 