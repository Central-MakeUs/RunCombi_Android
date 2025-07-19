package com.combo.runcombi.main.component

import android.util.Log
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
    val backStackEntryState =
        mainTabNavigator.navController.currentBackStackEntryFlow.collectAsState(initial = null)

    Scaffold(
        bottomBar = {
            val isTopLevelTab =
                MainTab.entries.any { it.isSelected(backStackEntryState.value?.destination) }
            if (isTopLevelTab) {
                MainBottomNavigationBar(
                    currentDestination = backStackEntryState.value?.destination,
                    onTabClick = { mainTab ->
                        when (mainTab) {
                            MainTab.WALK -> mainTabNavigator.navigationToWalkMain()
                            MainTab.HISTORY -> mainTabNavigator.navigationToHistory()

                            MainTab.SETTING -> mainTabNavigator.navigationToSetting()
                        }
                    }
                )
            }
        },
        content = { paddingValues ->
            MainTabNavHost(
                mainNavigator = navigator,
                mainTabNavigator = mainTabNavigator,
                padding = paddingValues,
                mainTabDataModel = mainTabDataModel
            )
        }
    )
}

