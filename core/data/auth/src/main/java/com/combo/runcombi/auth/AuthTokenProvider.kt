package com.combo.runcombi.auth


import com.combo.runcombi.datastore.datasource.AuthDataSource
import com.combo.runcombi.network.TokenProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthTokenProvider @Inject constructor(
    private val authDataSource: AuthDataSource,
) : TokenProvider {
    override suspend fun getAccessToken(): String? {
        android.util.Log.d("AuthTokenProvider", "getAccessToken 호출")
        val token = authDataSource.getAccessToken().firstOrNull()
        android.util.Log.d("AuthTokenProvider", "getAccessToken 결과: ${token?.take(20)}...")
        return token
    }

    override suspend fun getRefreshToken(): String? {
        return authDataSource.getRefreshToken().firstOrNull()
    }

    override suspend fun setAccessToken(accessToken: String) {
        authDataSource.setAccessToken(accessToken).collect()
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        authDataSource.setRefreshToken(refreshToken).collect()
    }
}
