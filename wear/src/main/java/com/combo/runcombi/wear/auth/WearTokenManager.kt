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
    companion object {
        // 하드코딩된 실제 토큰
        private const val HARDCODED_ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3NTg1NDU5MDcsInN1YiI6IjEzMiIsImV4cCI6MTc1OTE1MDcwNywicm9sZSI6IlVTRVIifQ.1Vpd4jIy36CBWsVxasKOWGLegZMWengjJyd8rL4EmHnd7OcLz1Us0ZYX3uwNfhBPMKgpV0upPei-bq_IUbkGtg"
        private const val HARDCODED_REFRESH_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3NTg1MzYyNTAsInN1YiI6Ijc5IiwiZXhwIjoxNzYxMTI4MjUwLCJtZW1iZXJJZCI6NzksInJvbGUiOiJVU0VSIn0.f9-eYiqwJ_c5kdZAbHr6j07DYEMVWjkSMpQzGqQbIkLWcHKDHsJ8K8FXSUC2yyO7QjR1xt143cp5lHhbTytnIA"
    }

    suspend fun initializeTokens() {
        // 앱 시작 시 하드코딩된 토큰을 저장
        authDataSource.setAccessToken(HARDCODED_ACCESS_TOKEN).collect()
        authDataSource.setRefreshToken(HARDCODED_REFRESH_TOKEN).collect()
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
}
