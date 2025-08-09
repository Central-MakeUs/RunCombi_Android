package com.combo.runcombi.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.history.screen.AddRecordScreen
import com.combo.runcombi.history.screen.EditRecordScreen
import com.combo.runcombi.history.screen.HistoryScreen
import com.combo.runcombi.history.screen.MemoScreen
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

fun NavController.navigateToAddRecord(
    date: String,
    navOptions: NavOptions? = null,
) {
    val route = RouteModel.MainTabRoute.HistoryRouteModel.AddRecord(date = date)
    this.navigate(route, navOptions)
}


fun NavController.navigateToEditRecord(
    runId: Int,
    navOptions: NavOptions? = null,
) {
    val route = RouteModel.MainTabRoute.HistoryRouteModel.EditRecord(runId = runId)
    this.navigate(route, navOptions)
}

fun NavController.navigateToMemo(
    runId: Int,
    memo: String,
    navOptions: NavOptions? = null,
) {
    val route = RouteModel.MainTabRoute.HistoryRouteModel.Memo(runId = runId, memo = memo)
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.historyNavGraph(
    onRecordClick: (Int) -> Unit,
    onAddClick: (String) -> Unit,
    onMemo: (Int, String) -> Unit,
    onEditRecord: (Int) -> Unit,
    onBack: () -> Unit,
) {
    navigation<MainTabDataModel.History>(
        startDestination = RouteModel.MainTabRoute.HistoryRouteModel.History,
    ) {
        composable<RouteModel.MainTabRoute.HistoryRouteModel.History> {
            HistoryScreen(onRecordClick = onRecordClick, onAddClick = onAddClick)
        }
        composable<RouteModel.MainTabRoute.HistoryRouteModel.Record>(
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<RouteModel.MainTabRoute.HistoryRouteModel.Record>()
            RecordScreen(
                runId = route.runId,
                onMemo = onMemo,
                onEditRecord = onEditRecord,
                onBack = onBack
            )
        }

        composable<RouteModel.MainTabRoute.HistoryRouteModel.AddRecord>(
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<RouteModel.MainTabRoute.HistoryRouteModel.AddRecord>()
            AddRecordScreen(
                date =  route.date,
                onBack = onBack
            )
        }

        composable<RouteModel.MainTabRoute.HistoryRouteModel.EditRecord>(
        ) { backStackEntry ->
            val route =
                backStackEntry.toRoute<RouteModel.MainTabRoute.HistoryRouteModel.EditRecord>()
            EditRecordScreen(
                runId = route.runId,
                onBack = onBack
            )
        }

        composable<RouteModel.MainTabRoute.HistoryRouteModel.Memo>(
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<RouteModel.MainTabRoute.HistoryRouteModel.Memo>()
            MemoScreen(
                runId = route.runId,
                memo = route.memo,
                onBack = onBack
            )
        }
    }
}
