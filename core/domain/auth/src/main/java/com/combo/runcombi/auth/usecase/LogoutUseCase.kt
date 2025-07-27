package com.combo.runcombi.auth.usecase

import com.combo.runcombi.auth.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke() = flow {
        emit(authRepository.requestLogout())
    }
}