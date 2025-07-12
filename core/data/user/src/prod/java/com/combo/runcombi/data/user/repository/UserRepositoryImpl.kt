package com.combo.runcombi.data.user.repository

import com.combo.runcombi.domain.user.model.User
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor() : UserRepository {
    override fun getUserInfo(): DomainResult<User> {
        TODO("Not yet implemented")
    }
} 