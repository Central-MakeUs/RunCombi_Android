package com.combo.runcombi.network

interface TokenExpirationHandler {
    suspend fun handleRefreshTokenExpiration()
}
