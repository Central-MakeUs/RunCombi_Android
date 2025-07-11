package com.combo.runcombi.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.combo.runcombi.history.navigation.navigateToHistory
import com.combo.runcombi.setting.navigation.navigateToSetting
import com.combo.runcombi.walk.navigation.navigateToWalkCountDown
import com.combo.runcombi.walk.navigation.navigateToWalkMain
import com.combo.runcombi.walk.navigation.navigateToWalkResult
import com.combo.runcombi.walk.navigation.navigateToWalkTracking

class MainTabNavigator(
    val navController: NavHostController,
) {

    fun navigationToHistory(navOptions: NavOptions) {
        navController.navigateToHistory(navOptions)
    }

    fun navigationToSetting(navOptions: NavOptions) {
        navController.navigateToSetting(navOptions)
    }

    fun navigationToWalkMain(navOptions: NavOptions) {
        navController.navigateToWalkMain(navOptions)
    }

    fun navigationToWalkCountdown() {
        navController.navigateToWalkCountDown()
    }

    fun navigationToWalkTracking() {
        navController.navigateToWalkTracking()
    }

    fun navigationToWalkResult() {
        navController.navigateToWalkResult()
    }
}

@Composable
internal fun rememberMainTabNavigator(
    navController: NavHostController = rememberNavController(),
): MainTabNavigator = remember(navController) {
    MainTabNavigator(navController)
}

