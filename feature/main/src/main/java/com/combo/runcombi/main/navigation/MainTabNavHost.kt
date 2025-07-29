package com.combo.runcombi.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.history.navigation.historyNavGraph
import com.combo.runcombi.setting.navigation.settingNavGraph
import com.combo.runcombi.walk.navigation.walkNavGraph

@Composable
fun MainTabNavHost(
    mainNavigator: MainNavigator,
    mainTabDataModel: MainTabDataModel,
    mainTabNavigator: MainTabNavigator,
    padding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = mainTabNavigator.navController,
        startDestination = mainTabDataModel
    ) {
        historyNavGraph(onRecordClick = {
            mainTabNavigator.navigationToRecord(runId = it, navOptions = null)
        })

        walkNavGraph(
            navController = mainTabNavigator.navController,
            onStartWalk = {
                mainTabNavigator.navigationToWalkTypeSelect()
            },
            onTypeSelected = {
                mainTabNavigator.navigationToWalkReady()
            },
            onCompleteReady = {
                mainTabNavigator.navigationToWalkCountdown()
            },
            onCountdownFinished = {
                mainTabNavigator.navigationToWalkTracking()
            },
            onFinish = {
                mainTabNavigator.navigationToWalkResult()
            },
            onBack = {
                mainTabNavigator.navController.popBackStack()
            },
            onNavigateToRecord = {
                mainTabNavigator.navigationToRecord(it, androidx.navigation.navOptions {
                    popUpTo(RouteModel.MainTabRoute.WalkRouteModel.WalkMain) {
                        inclusive = false
                    }
                })
            }
        )

        settingNavGraph(
            onClickSetting = {
                mainTabNavigator.navigationToSetting()
            },
            goToLogin = {
                mainNavigator.navigateToLogin()
            },
            onBack = {
                mainTabNavigator.navController.popBackStack()
            },
        )
    }
}
