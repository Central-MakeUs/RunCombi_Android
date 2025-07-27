package com.combo.runcombi.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.setting.screen.MyScreen
import com.combo.runcombi.setting.screen.SettingScreen

fun NavController.navigateToSettingMain(
    navOptions: NavOptions? = androidx.navigation.navOptions {
        popUpTo(this@navigateToSettingMain.graph.id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    },
) {
    this.navigate(MainTabDataModel.Setting, navOptions)
}

fun NavController.navigateToSetting() {
    this.navigate(RouteModel.MainTabRoute.SettingRouteModel.Setting)
}

fun NavGraphBuilder.settingNavGraph(
    onClickSetting: () -> Unit = {},
    goToLogin: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    navigation<MainTabDataModel.Setting>(
        startDestination = RouteModel.MainTabRoute.SettingRouteModel.My,
    ) {
        composable<RouteModel.MainTabRoute.SettingRouteModel.My> {
            MyScreen(onClickSetting = onClickSetting)
        }

        composable<RouteModel.MainTabRoute.SettingRouteModel.Setting> {
            SettingScreen(
                goToLogin = goToLogin,
                onBack = onBack)
        }
    }
}
