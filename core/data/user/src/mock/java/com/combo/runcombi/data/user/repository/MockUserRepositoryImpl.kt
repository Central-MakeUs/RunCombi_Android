package com.combo.runcombi.data.user.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.User
import com.combo.runcombi.domain.user.repository.UserRepository
import javax.inject.Inject

class MockUserRepositoryImpl @Inject constructor() : UserRepository {
    override fun getUserInfo(): DomainResult<User> {
        return DomainResult.Success(
            User(
                nickname = "창스",
                gender = Gender.MALE,
                height = 180,
                weight = 80,
                profileImageUrl = null
            )
        )
    }
} 