package com.combo.runcombi.walk.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.walk.screen.WalkCountdownScreen
import com.combo.runcombi.walk.screen.WalkMainScreen
import com.combo.runcombi.walk.screen.WalkResultScreen
import com.combo.runcombi.walk.screen.WalkTrackingScreen
import com.combo.runcombi.walk.viewmodel.WalkRecordViewModel

fun NavController.navigateToWalkMain(
    navOptions: NavOptions,
) {
    this.navigate(MainTabDataModel.Walk, navOptions)
}

fun NavController.navigateToWalkCountDown() {
    this.navigate(RouteModel.MainTabRoute.WalkRouteModel.WalkCountdown)
}

fun NavController.navigateToWalkTracking() {
    this.navigate(RouteModel.MainTabRoute.WalkRouteModel.Walk) {
        popUpTo(RouteModel.MainTabRoute.WalkRouteModel.WalkCountdown) { inclusive = true }
    }
}


fun NavController.navigateToWalkResult() {
    this.navigate(RouteModel.MainTabRoute.WalkRouteModel.WalkRoute.WalkResult) {
        popUpTo(RouteModel.MainTabRoute.WalkRouteModel.WalkRoute.WalkTracking) { inclusive = true }
    }
}

fun NavGraphBuilder.walkNavGraph(
    navController: NavController,
    onStartWalk: () -> Unit,
    onCountdownFinished: () -> Unit,
    onFinish: () -> Unit,
) {
    navigation<MainTabDataModel.Walk>(
        startDestination = RouteModel.MainTabRoute.WalkRouteModel.WalkMain,
    ) {
        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkMain> {
            WalkMainScreen(
                onStartWalk = onStartWalk
            )
        }

        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkCountdown> {
            WalkCountdownScreen(
                onCountdownFinished = onCountdownFinished
            )
        }

        walkTrackingNavGraph(navController, onFinish)
    }
}

fun NavGraphBuilder.walkTrackingNavGraph(
    navController: NavController,
    onFinish: () -> Unit,
) {
    navigation<RouteModel.MainTabRoute.WalkRouteModel.Walk>(
        startDestination = RouteModel.MainTabRoute.WalkRouteModel.WalkRoute.WalkTracking,
    ) {
        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkRoute.WalkTracking> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(RouteModel.MainTabRoute.WalkRouteModel.Walk)
            }
            val walkRecordViewModel = hiltViewModel<WalkRecordViewModel>(parentEntry)
            WalkTrackingScreen(
                walkRecordViewModel = walkRecordViewModel,
                onFinish = onFinish
            )
        }
        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkRoute.WalkResult> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(RouteModel.MainTabRoute.WalkRouteModel.Walk)
            }
            val walkRecordViewModel = hiltViewModel<WalkRecordViewModel>(parentEntry)
            WalkResultScreen(
                walkRecordViewModel = walkRecordViewModel
            )
        }
    }
}
