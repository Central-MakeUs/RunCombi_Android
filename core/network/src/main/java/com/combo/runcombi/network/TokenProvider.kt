package com.combo.runcombi.network

interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken() : String?
    suspend fun setAccessToken(accessToken : String)
    suspend fun setRefreshToken(refreshToken : String)
}
