package com.combo.runcombi.network.interceptor

import com.combo.runcombi.network.TokenExpirationHandler
import com.combo.runcombi.network.TokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenReissueInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val tokenExpirationHandler : TokenExpirationHandler
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().apply {
            runBlocking {
                val token = tokenProvider.getRefreshToken()
                token?.let {
                    addHeader("Authorization", "Bearer $it")
                }
            }
        }
        val response = chain.proceed(newRequest.build())

        when (response.code) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                runBlocking {
                    tokenExpirationHandler.handleRefreshTokenExpiration()
                }
                return response
            }
        }
        return response
    }
}
