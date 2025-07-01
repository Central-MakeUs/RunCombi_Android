package com.combo.runcombi.core.navigation

import com.combo.runcombi.core.navigation.model.RouteModel
import kotlinx.coroutines.flow.SharedFlow

interface NavigationEventHandler {
    val routeToNavigate: SharedFlow<RouteModel>

    suspend fun triggerRouteToNavigate(route: RouteModel)
}
