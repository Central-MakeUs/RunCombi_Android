package com.combo.runcombi.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.history.HistoryScreen

fun NavController.navigateToHistory(
    navOptions: NavOptions? = androidx.navigation.navOptions {
        popUpTo(this@navigateToHistory.graph.id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    },
) {
    this.navigate(MainTabDataModel.History, navOptions)
}

fun NavGraphBuilder.historyNavGraph() {
    navigation<MainTabDataModel.History>(
        startDestination = RouteModel.MainTabRoute.HistoryRouteModel.History,
    ) {
        composable<RouteModel.MainTabRoute.HistoryRouteModel.History> {
            HistoryScreen()
        }
    }
}
