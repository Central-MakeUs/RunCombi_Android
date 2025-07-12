package com.combo.runcombi.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.history.navigation.historyNavGraph
import com.combo.runcombi.setting.navigation.settingNavGraph
import com.combo.runcombi.walk.navigation.walkNavGraph

@Composable
fun MainTabNavHost(
    mainNavigator: MainNavigator,
    mainTabNavigator: MainTabNavigator,
    padding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = mainTabNavigator.navController,
        startDestination = MainTabDataModel.Walk
    ) {
        historyNavGraph()

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
        )

        settingNavGraph()
    }
}
