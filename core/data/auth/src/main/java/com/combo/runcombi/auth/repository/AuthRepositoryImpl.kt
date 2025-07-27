package com.combo.runcombi.auth.repository


import com.combo.runcombi.auth.mapper.toDomainModel
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.common.convert
import com.combo.runcombi.common.handleResult
import com.combo.runcombi.datastore.datasource.AuthDataSource
import com.combo.runcombi.network.model.request.KakaoLoginRequest
import com.combo.runcombi.network.service.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val authDataSource: AuthDataSource,
) : AuthRepository {

    override suspend fun requestKakaoLogin(token: String) = handleResult {
        val request = KakaoLoginRequest(
            kakaoAccessToken = token
        )
        authService.requestKakaoLogin(request)
    }.convert {
        it.toDomainModel()
    }

    override suspend fun requestWithdraw() = handleResult {
        authService.requestWithdraw()
    }.convert {}

    override fun getAccessToken(): Flow<String?> = authDataSource.getAccessToken()

    override fun getRefreshToken(): Flow<String?> = authDataSource.getRefreshToken()

    override fun setAccessToken(accessToken: String): Flow<Unit> =
        authDataSource.setAccessToken(accessToken)

    override fun setRefreshToken(refreshToken: String): Flow<Unit> =
        authDataSource.setRefreshToken(refreshToken)

    override suspend fun requestLogout() {
        authDataSource.deleteAccessToken()
        authDataSource.deleteRefreshToken()
    }
}
