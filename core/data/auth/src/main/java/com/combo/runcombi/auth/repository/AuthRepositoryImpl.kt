package com.combo.runcombi.auth.repository


import com.combo.runcombi.auth.mapper.toDomainModel
import com.combo.runcombi.common.convert
import com.combo.runcombi.common.handleResult
import com.combo.runcombi.datastore.datasource.AuthDataSource
import com.combo.runcombi.network.model.request.KakaoLoginRequest
import com.combo.runcombi.network.service.LoginService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
    private val authDataSource: AuthDataSource,
) : AuthRepository {

    override suspend fun requestKakaoLogin(token: String) = handleResult {
        val request = KakaoLoginRequest(
            kakaoAccessToken = token
        )
        loginService.requestKakaoLogin(request)
    }.convert {
        it.toDomainModel()
    }

    override fun getAccessToken(): Flow<String?> = authDataSource.getAccessToken()

    override fun getRefreshToken(): Flow<String?> = authDataSource.getRefreshToken()

    override fun setAccessToken(accessToken: String): Flow<Unit> =
        authDataSource.setAccessToken(accessToken)

    override fun setRefreshToken(refreshToken: String): Flow<Unit> =
        authDataSource.setRefreshToken(refreshToken)
}
