package com.combo.runcombi.domain.user.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.User

interface UserRepository {
    fun getUserInfo(): DomainResult<User>
}
