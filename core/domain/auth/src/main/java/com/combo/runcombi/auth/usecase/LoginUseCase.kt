package com.combo.runcombi.auth.usecase

import com.combo.runcombi.auth.model.KakaoLogin
import com.combo.runcombi.auth.repository.AuthRepository
import com.combo.runcombi.common.DomainResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend fun invoke(token: String): KakaoLogin? {
        return when (val response = authRepository.requestKakaoLogin(token)) {
            is DomainResult.Success -> {
                response.data.also {
                    authRepository.setAccessToken(it.accessToken).first()
                    authRepository.setRefreshToken(it.refreshToken).first()
                }
            }

            is DomainResult.Error, is DomainResult.Exception -> null
        }
    }
}
