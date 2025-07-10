package com.combo.runcombi.main.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.main.navigation.MainNavigator
import com.combo.runcombi.main.navigation.MainTabNavHost
import com.combo.runcombi.main.navigation.MainTabNavigator
import com.combo.runcombi.main.navigation.rememberMainTabNavigator

@Composable
fun MainTabContent(
    navigator: MainNavigator,
    mainTabDataModel: MainTabDataModel,
    modifier: Modifier = Modifier,
    mainTabNavigator: MainTabNavigator = rememberMainTabNavigator(),
) {

    val backStackEntryState =
        mainTabNavigator.navController.currentBackStackEntryFlow.collectAsState(initial = null)
    val navigationBottomPadding =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LaunchedEffect(mainTabDataModel) {
        when (mainTabDataModel) {
            is MainTabDataModel.Walk -> {
                mainTabNavigator.navigationToWalk()
            }

            MainTabDataModel.History -> {
                mainTabNavigator.navigationToHistory()
            }

            MainTabDataModel.Setting -> {
                mainTabNavigator.navigationToSetting()
            }

            MainTabDataModel.None -> return@LaunchedEffect
        }
    }

    Scaffold(
        bottomBar = {
            MainBottomNavigationBar(
                currentDestination = backStackEntryState.value?.destination,
                onTabClick = { mainTab ->
                    when (mainTab) {
                        MainTab.WALK -> {
                            mainTabNavigator.navigationToWalk()
                        }

                        MainTab.HISTORY -> {
                            mainTabNavigator.navigationToHistory()
                        }

                        MainTab.SETTING -> {
                            mainTabNavigator.navigationToSetting()
                        }
                    }
                }
            )
        },
        content = { padding ->
            MainTabNavHost(
                mainNavigator = navigator,
                mainTabNavigator = mainTabNavigator,
                padding = padding
            )
        }
    )
}

