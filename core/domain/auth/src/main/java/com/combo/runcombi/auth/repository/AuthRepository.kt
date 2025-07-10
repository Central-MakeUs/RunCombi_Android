package com.combo.runcombi.auth.repository

import com.combo.runcombi.auth.model.KakaoLogin
import com.combo.runcombi.common.DomainResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun requestKakaoLogin(token: String): DomainResult<KakaoLogin>

    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>

    fun setAccessToken(accessToken: String): Flow<Unit>
    fun setRefreshToken(refreshToken: String): Flow<Unit>
}
