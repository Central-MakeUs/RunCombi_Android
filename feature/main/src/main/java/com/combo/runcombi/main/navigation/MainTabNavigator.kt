package com.combo.runcombi.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.combo.runcombi.history.navigation.navigateToHistory
import com.combo.runcombi.setting.navigation.navigateToSetting
import com.combo.runcombi.walk.navigation.navigateToWalk

class MainTabNavigator(
    val navController: NavHostController,
) {

    fun navigationToHistory() {
        navController.navigateToHistory()
    }

    fun navigationToSetting() {
        navController.navigateToSetting()
    }

    fun navigationToWalk() {
        navController.navigateToWalk()
    }
}

@Composable
internal fun rememberMainTabNavigator(
    navController: NavHostController = rememberNavController(),
): MainTabNavigator = remember(navController) {
    MainTabNavigator(navController)
}

