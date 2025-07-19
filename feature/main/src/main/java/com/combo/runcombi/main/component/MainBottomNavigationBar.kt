package com.combo.runcombi.main.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.combo.runcombi.core.designsystem.R
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.ui.ext.clickableWithoutRipple
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.core.navigation.model.RouteModel.MainTabRoute

internal const val MAIN_BOTTOM_NAVIGATION_BAR_HEIGHT = 72

enum class MainTab(
    val routeModel: RouteModel,
    @DrawableRes val defaultIconRes: Int,
    @DrawableRes val selectedIconRes: Int,
) {
    HISTORY(
        routeModel = MainTabRoute.HistoryRouteModel.History,
        defaultIconRes = R.drawable.ic_history,
        selectedIconRes = R.drawable.ic_history_selected
    ),
    WALK(
        routeModel = MainTabRoute.WalkRouteModel.WalkMain,
        defaultIconRes = R.drawable.ic_walk,
        selectedIconRes = R.drawable.ic_walk_selected
    ),
    SETTING(
        routeModel = MainTabRoute.SettingRouteModel.Setting,
        defaultIconRes = R.drawable.ic_setting,
        selectedIconRes = R.drawable.ic_setting_selected
    );

    fun isSelected(destination: NavDestination?): Boolean {
        val currentDestinationRoute = destination?.route ?: return false
        return containsWithRoute(currentDestinationRoute)
    }

    private fun containsWithRoute(route: String): Boolean {
        val target = when (this) {
            WALK -> MainTabRoute.WalkRouteModel.WalkMain::class.qualifiedName
            HISTORY -> MainTabRoute.HistoryRouteModel.History::class.qualifiedName
            SETTING -> MainTabRoute.SettingRouteModel.Setting::class.qualifiedName
        } ?: return false
        return route == target
    }
}

@Composable
fun MainBottomNavigationBar(
    currentDestination: NavDestination?,
    onTabClick: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Surface(
        modifier = modifier.height(MAIN_BOTTOM_NAVIGATION_BAR_HEIGHT.dp),
        color = Grey01
    ) {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Grey02
        )
        Row {
            MainTab.entries.forEach { mainTab ->
                val selected = mainTab.isSelected(currentDestination)
                MainBottomNavigationBarItem(
                    modifier = Modifier.weight(1f),
                    icon = {
                        StableImage(
                            modifier = Modifier.height(34.dp),
                            drawableResId = if (selected) mainTab.selectedIconRes else mainTab.defaultIconRes,
                            description = mainTab.name,
                        )
                    },
                    onClick = {
                        onTabClick(mainTab)
                    }
                )
            }
        }
    }
}

@Composable
fun MainBottomNavigationBarItem(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickableWithoutRipple {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainBottomNavigationBar() {
    MainBottomNavigationBar(
        currentDestination = null,
        onTabClick = {}
    )
}

