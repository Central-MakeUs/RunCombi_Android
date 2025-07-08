package com.combo.runcombi.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.history.HistoryScreen

fun NavController.navigateToHistory() {
    this.navigate(MainTabDataModel.History) {
        popUpTo(this@navigateToHistory.graph.id) {
            inclusive = true
        }
    }
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
