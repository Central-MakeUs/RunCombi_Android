package com.combo.runcombi.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.history.screen.HistoryScreen
import com.combo.runcombi.history.screen.RecordScreen

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

fun NavController.navigateToRecord(
    runId: Int,
    navOptions: NavOptions? = null,
) {
    val route = RouteModel.MainTabRoute.HistoryRouteModel.Record(runId = runId)
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.historyNavGraph(onRecordClick: (Int) -> Unit, onBack: () -> Unit) {
    navigation<MainTabDataModel.History>(
        startDestination = RouteModel.MainTabRoute.HistoryRouteModel.History,
    ) {
        composable<RouteModel.MainTabRoute.HistoryRouteModel.History> {
            HistoryScreen(onRecordClick = onRecordClick)
        }
        composable<RouteModel.MainTabRoute.HistoryRouteModel.Record>(
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<RouteModel.MainTabRoute.HistoryRouteModel.Record>()
            RecordScreen(
                runId = route.runId,
                onBack = onBack
            )
        }
    }
}
