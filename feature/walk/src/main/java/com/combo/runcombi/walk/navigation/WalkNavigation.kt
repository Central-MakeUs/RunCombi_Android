package com.combo.runcombi.walk.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.walk.WalkMainScreen

fun NavController.navigateToWalk() {
    this.navigate(MainTabDataModel.Walk) {
        popUpTo(this@navigateToWalk.graph.id) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.walkNavGraph() {
    navigation<MainTabDataModel.Walk>(
        startDestination = RouteModel.MainTabRoute.WalkRouteModel.Walk,
    ) {
        composable<RouteModel.MainTabRoute.WalkRouteModel.Walk> {
            WalkMainScreen()
        }
    }
}
