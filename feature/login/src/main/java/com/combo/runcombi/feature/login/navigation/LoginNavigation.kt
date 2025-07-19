package com.combo.runcombi.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.feature.login.LoginRoute

fun NavController.navigateToLogin() {
    this.navigate(RouteModel.Login) {
        popUpTo(this@navigateToLogin.graph.id) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.loginNavGraph(
    onLoginSuccess: (MemberStatus) -> Unit,
) {
    composable<RouteModel.Login> {
        LoginRoute(
            onLoginSuccess = onLoginSuccess
        )
    }
}
