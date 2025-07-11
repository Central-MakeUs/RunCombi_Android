package com.combo.runcombi.main.component

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.main.navigation.MainNavigator
import com.combo.runcombi.main.navigation.MainTabNavHost
import com.combo.runcombi.main.navigation.MainTabNavigator
import com.combo.runcombi.main.navigation.rememberMainTabNavigator

@Composable
fun MainTabContent(
    navigator: MainNavigator,
    mainTabDataModel: MainTabDataModel,
    mainTabNavigator: MainTabNavigator = rememberMainTabNavigator(),
) {
    val topLevelNavOptions = navOptions {
        popUpTo(navigator.navController.graph.findStartDestination().id) {
            inclusive = true
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    val backStackEntryState =
        mainTabNavigator.navController.currentBackStackEntryFlow.collectAsState(initial = null)

    val navigateToTab: (MainTabDataModel) -> Unit = { tab ->
        when (tab) {
            is MainTabDataModel.Walk -> mainTabNavigator.navigationToWalkMain(topLevelNavOptions)
            MainTabDataModel.History -> mainTabNavigator.navigationToHistory(topLevelNavOptions)
            MainTabDataModel.Setting -> mainTabNavigator.navigationToSetting(topLevelNavOptions)
            MainTabDataModel.None -> Unit
        }
    }

    LaunchedEffect(mainTabDataModel) {
        navigateToTab(mainTabDataModel)
    }

    Scaffold(
        bottomBar = {
            MainBottomNavigationBar(
                currentDestination = backStackEntryState.value?.destination,
                onTabClick = { mainTab ->
                    when (mainTab) {
                        MainTab.WALK -> mainTabNavigator.navigationToWalkMain(topLevelNavOptions)
                        MainTab.HISTORY -> mainTabNavigator.navigationToHistory(topLevelNavOptions)
                        MainTab.SETTING -> mainTabNavigator.navigationToSetting(topLevelNavOptions)
                    }
                }
            )
        },
        content = { paddingValues ->
            MainTabNavHost(
                mainNavigator = navigator,
                mainTabNavigator = mainTabNavigator,
                padding = paddingValues
            )
        }
    )
}

