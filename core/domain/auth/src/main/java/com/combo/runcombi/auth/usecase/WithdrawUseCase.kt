package com.combo.runcombi.auth.usecase

import com.combo.runcombi.auth.repository.AuthRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke() = flow {
        emit(authRepository.requestWithdraw())
    }
}