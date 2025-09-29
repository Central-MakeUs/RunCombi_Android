package com.combo.runcombi.wear.auth

import com.combo.runcombi.datastore.datasource.AuthDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WearTokenManager @Inject constructor(
    private val authDataSource: AuthDataSource
) {

    suspend fun initializeTokens() {
        // 하드코딩된 토큰 초기화 비활성화
        // 모바일로부터 토큰을 받아서 사용
        android.util.Log.d("WearTokenManager", "하드코딩된 토큰 초기화 비활성화됨")
    }

    suspend fun getAccessToken(): String? {
        return authDataSource.getAccessToken().first()
    }

    suspend fun getRefreshToken(): String? {
        return authDataSource.getRefreshToken().first()
    }

    suspend fun clearTokens() {
        authDataSource.deleteAccessToken()
        authDataSource.deleteRefreshToken()
    }

    suspend fun saveAccessTokenFromMobile(accessToken: String) {
        android.util.Log.d("WearTokenManager", "Saving access token from mobile: ${accessToken.take(20)}...")
        authDataSource.setAccessToken(accessToken).collect()
    }
}
