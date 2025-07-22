package com.combo.runcombi.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.combo.runcombi.history.navigation.navigateToHistory
import com.combo.runcombi.history.navigation.navigateToRecord
import com.combo.runcombi.setting.navigation.navigateToSetting
import com.combo.runcombi.walk.navigation.navigateToWalkCountDown
import com.combo.runcombi.walk.navigation.navigateToWalkMain
import com.combo.runcombi.walk.navigation.navigateToWalkReady
import com.combo.runcombi.walk.navigation.navigateToWalkResult
import com.combo.runcombi.walk.navigation.navigateToWalkTracking
import com.combo.runcombi.walk.navigation.navigateToWalkTypeSelect

class MainTabNavigator(
    val navController: NavHostController,
) {
    fun navigationToHistory() {
        navController.navigateToHistory()
    }

    fun navigationToRecord(imagePaths: List<String>) {
        navController.navigateToRecord(imagePaths)
    }

    fun navigationToSetting() {
        navController.navigateToSetting()
    }

    fun navigationToWalkMain() {
        navController.navigateToWalkMain()
    }

    fun navigationToWalkTypeSelect() {
        navController.navigateToWalkTypeSelect()
    }

    fun navigationToWalkReady() {
        navController.navigateToWalkReady()
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

