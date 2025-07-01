package com.combo.runcombi.main.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.combo.runcombi.feature.login.navigateToLogin
import com.combo.runcombi.onboarding.navigateToOnboarding

class MainNavigator(
    val navController: NavHostController,
) {
    fun popBackStack() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
            navController.popBackStack()
        }
    }

    fun navigateToLogin() {
        navController.navigateToLogin()
    }

    fun navigationToOnboarding() {
        navController.navigateToOnboarding()
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}

