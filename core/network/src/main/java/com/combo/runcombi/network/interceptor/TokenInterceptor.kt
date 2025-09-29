package com.combo.runcombi.network.interceptor

import com.combo.runcombi.network.TokenProvider
import com.combo.runcombi.network.model.request.TokenReissueRequest
import com.combo.runcombi.network.service.TokenService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
    private val tokenService: TokenService,
    private val tokenProvider: TokenProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        android.util.Log.d("TokenInterceptor", "API 요청 시작: ${chain.request().url}")
        
        val newRequest = chain.request().newBuilder().apply {
            runBlocking {
                val token = tokenProvider.getAccessToken()
                android.util.Log.d("TokenInterceptor", "토큰 확인: ${token?.take(20)}...")
                token?.let {
                    addHeader("Authorization", "Bearer $it")
                    android.util.Log.d("TokenInterceptor", "Authorization 헤더 추가됨")
                }
            }
        }

        android.util.Log.d("TokenInterceptor", "실제 API 호출 시작")
        val response = chain.proceed(newRequest.build())

        when (response.code) {
            HttpURLConnection.HTTP_OK -> {
                val newAccessToken: String =
                    response.header("Authorization", null) ?: return response
                CoroutineScope(Dispatchers.IO).launch {
                    val existedAccessToken = tokenProvider.getAccessToken()
                    if (existedAccessToken != newAccessToken) {
                        tokenProvider.setAccessToken(newAccessToken)
                    }
                }
            }

            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                val retryRequest = chain.request().newBuilder().apply {
                    runBlocking {
                        tokenProvider.getRefreshToken()?.let {
                            val newToken = tokenService.requestTokenReissue()
                            if (newToken.isSuccessful) {
                                newToken.body()?.let { token ->
                                    addHeader("Authorization", "Bearer ${token.accessToken}")
                                    CoroutineScope(Dispatchers.IO).launch {
                                        tokenProvider.setAccessToken(token.accessToken)
                                        tokenProvider.setRefreshToken(token.refreshToken)
                                    }
                                }
                            }
                        }
                    }
                }
                return chain.proceed(retryRequest.build())
            }
        }
        return response
    }
}
