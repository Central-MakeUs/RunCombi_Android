package com.combo.runcombi.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.combo.runcombi.core.navigation.model.RouteModel

fun NavController.navigateToLogin() {
    this.navigate(RouteModel.Login) {
        popUpTo(this@navigateToLogin.graph.id) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.loginNavGraph(
    onLoginSuccess: () -> Unit,
) {
    composable<RouteModel.Login> {
        LoginRoute(
            onLoginSuccess = onLoginSuccess
        )
    }
}
