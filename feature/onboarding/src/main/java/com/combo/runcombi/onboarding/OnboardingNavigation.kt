package com.combo.runcombi.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.combo.runcombi.core.navigation.model.RouteModel

fun NavController.navigateToOnboarding() {
    this.navigate(RouteModel.Onboarding) {
        popUpTo(this@navigateToOnboarding.graph.id) {
            inclusive = true
        }
    }
}


fun NavGraphBuilder.onboardingNavGraph(
) {
    composable<RouteModel.Onboarding> {
        OnboardingRoute()
    }
}
