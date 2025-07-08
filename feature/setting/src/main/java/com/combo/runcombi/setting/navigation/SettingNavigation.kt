package com.combo.runcombi.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.setting.SettingScreen

fun NavController.navigateToSetting() {
    this.navigate(MainTabDataModel.Setting) {
        popUpTo(this@navigateToSetting.graph.id) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.settingNavGraph() {
    navigation<MainTabDataModel.Setting>(
        startDestination = RouteModel.MainTabRoute.SettingRouteModel.Setting,
    ) {
        composable<RouteModel.MainTabRoute.SettingRouteModel.Setting> {
            SettingScreen()
        }
    }
}
