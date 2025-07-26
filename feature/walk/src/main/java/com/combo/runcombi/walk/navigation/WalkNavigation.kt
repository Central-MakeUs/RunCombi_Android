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
import com.combo.runcombi.walk.screen.WalkReadyScreen
import com.combo.runcombi.walk.screen.WalkResultScreen
import com.combo.runcombi.walk.screen.WalkTrackingScreen
import com.combo.runcombi.walk.screen.WalkTypeSelectScreen
import com.combo.runcombi.walk.viewmodel.WalkMainViewModel

fun NavController.navigateToWalkMain(
    navOptions: NavOptions? = androidx.navigation.navOptions {
        popUpTo(this@navigateToWalkMain.graph.id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    },
) {
    this.navigate(MainTabDataModel.Walk, navOptions)
}

fun NavController.navigateToWalkTypeSelect() {
    this.navigate(RouteModel.MainTabRoute.WalkRouteModel.WalkTypeSelct)
}

fun NavController.navigateToWalkReady() {
    this.navigate(RouteModel.MainTabRoute.WalkRouteModel.WalkReady)
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
    onTypeSelected: () -> Unit,
    onCompleteReady: () -> Unit,
    onStartWalk: () -> Unit,
    onCountdownFinished: () -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit,
    onNavigateToRecord: (Int) -> Unit,
) {
    navigation<MainTabDataModel.Walk>(
        startDestination = RouteModel.MainTabRoute.WalkRouteModel.WalkMain,
    ) {
        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkMain> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainTabDataModel.Walk)
            }
            val walkMainViewModel = hiltViewModel<WalkMainViewModel>(parentEntry)
            WalkMainScreen(
                walkMainViewModel = walkMainViewModel,
                onStartWalk = onStartWalk
            )
        }

        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkTypeSelct> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainTabDataModel.Walk)
            }

            val walkMainViewModel = hiltViewModel<WalkMainViewModel>(parentEntry)
            WalkTypeSelectScreen(
                walkMainViewModel = walkMainViewModel,
                onBack = onBack,
                onTypeSelected = onTypeSelected
            )
        }

        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkReady> {
            WalkReadyScreen(
                onBack = onBack,
                onCompleteReady = onCompleteReady,
            )
        }

        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkCountdown> {
            WalkCountdownScreen(
                onCountdownFinished = onCountdownFinished
            )
        }

        walkTrackingNavGraph(navController, onFinish, onBack, onNavigateToRecord)
    }
}

fun NavGraphBuilder.walkTrackingNavGraph(
    navController: NavController,
    onFinish: () -> Unit,
    onBack: () -> Unit,
    onNavigateToRecord: (Int) -> Unit,
) {
    navigation<RouteModel.MainTabRoute.WalkRouteModel.Walk>(
        startDestination = RouteModel.MainTabRoute.WalkRouteModel.WalkRoute.WalkTracking,
    ) {
        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkRoute.WalkTracking> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainTabDataModel.Walk)
            }
            val walkMainViewModel = hiltViewModel<WalkMainViewModel>(parentEntry)
            WalkTrackingScreen(
                walkMainViewModel = walkMainViewModel,
                onFinish = onFinish,
                onBack = onBack
            )
        }
        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkRoute.WalkResult> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainTabDataModel.Walk)
            }
            val walkMainViewModel = hiltViewModel<WalkMainViewModel>(parentEntry)
            WalkResultScreen(
                walkMainViewModel = walkMainViewModel,
                onBack = onBack,
                onNavigateToRecord = onNavigateToRecord
            )
        }
    }
}
