package com.combo.runcombi.auth

import com.combo.runcombi.core.navigation.NavigationEventHandler
import com.combo.runcombi.core.navigation.di.AuthNavigation
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.datastore.datasource.AuthDataSource
import com.combo.runcombi.network.TokenExpirationHandler
import javax.inject.Inject

class AuthTokenExpirationHandler @Inject constructor(
    private val authDataSource: AuthDataSource,
    @AuthNavigation private val authNavigationEventHandler: NavigationEventHandler,
) : TokenExpirationHandler {

    override suspend fun handleRefreshTokenExpiration() {
        clearCachedData()
        authNavigationEventHandler.triggerRouteToNavigate(RouteModel.Login)
    }

    private suspend fun clearCachedData() {
        authDataSource.setAccessToken("")
        authDataSource.setRefreshToken("")
    }
}
