package com.combo.runcombi.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.main.navigation.MainNavHost
import com.combo.runcombi.main.navigation.MainNavigator
import com.combo.runcombi.main.navigation.rememberMainNavigator
import com.combo.runcombi.main.navigation.compareTo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
internal fun MainScreen(
    startDestination: RouteModel,
    navigator: MainNavigator = rememberMainNavigator(),
    viewModel: MainViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.debounce(200).collectLatest { route ->
            if (navigator.navController.currentDestination?.compareTo(route) == false) {
                if (route == RouteModel.Login) {
                    navigator.navigateToLogin()
                } else {
                    navigator.navController.navigate(route)
                }
            }
        }
    }

    MainScreenContent(
        navigator = navigator,
        startDestination = startDestination
    )
}

@Composable
private fun MainScreenContent(
    navigator: MainNavigator,
    startDestination: RouteModel,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        content = { padding ->
            MainNavHost(
                navigator = navigator,
                startDestination = startDestination,
                padding = padding
            )
        }
    )
}
