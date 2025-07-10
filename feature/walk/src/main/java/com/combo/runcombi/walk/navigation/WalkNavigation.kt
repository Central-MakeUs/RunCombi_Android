package com.combo.runcombi.walk.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.walk.screen.WalkCountdownScreen
import com.combo.runcombi.walk.screen.WalkMainScreen
import com.combo.runcombi.walk.screen.WalkResultScreen
import com.combo.runcombi.walk.screen.WalkTrackingScreen
import com.combo.runcombi.walk.viewmodel.WalkRecordViewModel

fun NavController.navigateToWalk(
    navOptions: NavOptions? = navOptions {
        popUpTo(this@navigateToWalk.graph.id) { inclusive = true }
    },
) {
    this.navigate(MainTabDataModel.Walk, navOptions)
}

fun NavController.navigateToWalkCountDown() {
    this.navigate(RouteModel.MainTabRoute.WalkRouteModel.WalkCountdown)
}

fun NavController.navigateToWalkTracking() {
    this.navigate(RouteModel.MainTabRoute.WalkRouteModel.WalkTracking) {
        popUpTo(RouteModel.MainTabRoute.WalkRouteModel.WalkCountdown) { inclusive = true }
    }
}


fun NavController.navigateToWalkResult() {
    this.navigate(RouteModel.MainTabRoute.WalkRouteModel.WalkResult) {
        popUpTo(RouteModel.MainTabRoute.WalkRouteModel.WalkTracking) { inclusive = true }
    }
}

fun NavGraphBuilder.walkNavGraph(
    navController: NavController,
    onStartWalk: () -> Unit,
    onCountdownFinished: () -> Unit,
    onFinish: () -> Unit,
) {
    navigation<MainTabDataModel.Walk>(
        startDestination = RouteModel.MainTabRoute.WalkRouteModel.Walk,
    ) {
        composable<RouteModel.MainTabRoute.WalkRouteModel.Walk> {
            WalkMainScreen(
                onStartWalk = onStartWalk
            )
        }

        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkCountdown> {
            WalkCountdownScreen(
                onCountdownFinished = onCountdownFinished
            )
        }

        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkTracking> { backStackEntry ->
            val walkRecordViewModel = hiltViewModel<WalkRecordViewModel>(backStackEntry)
            WalkTrackingScreen(
                walkRecordViewModel = walkRecordViewModel,
                onFinish = onFinish
            )
        }
        composable<RouteModel.MainTabRoute.WalkRouteModel.WalkResult> {
            val walkRecordViewModel: WalkRecordViewModel =
                if (navController.previousBackStackEntry != null) {
                    hiltViewModel(navController.previousBackStackEntry!!)
                } else {
                    hiltViewModel()
                }
            WalkResultScreen(
                walkRecordViewModel = walkRecordViewModel
            )
        }
    }
}
