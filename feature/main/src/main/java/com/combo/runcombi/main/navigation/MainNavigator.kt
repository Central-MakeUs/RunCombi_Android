package com.combo.runcombi.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.feature.login.navigation.navigateToLogin
import com.combo.runcombi.signup.navigation.navigateToSignup
import com.combo.runcombi.signup.navigation.navigateToSignupBody
import com.combo.runcombi.signup.navigation.navigateToSignupComplete
import com.combo.runcombi.signup.navigation.navigateToSignupGender
import com.combo.runcombi.signup.navigation.navigateToSignupInput
import com.combo.runcombi.signup.navigation.navigateToSignupPetInfo
import com.combo.runcombi.signup.navigation.navigateToSignupPetProfile
import com.combo.runcombi.signup.navigation.navigateToSignupPetStyle

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

    fun navigateToSignup() {
        navController.navigateToSignup()
    }

    fun navigateToSignupInput() {
        navController.navigateToSignupInput()
    }

    fun navigateToSignupGender() {
        navController.navigateToSignupGender()
    }

    fun navigateToSignupBody() {
        navController.navigateToSignupBody()
    }

    fun navigateToSignupPetProfile() {
        navController.navigateToSignupPetProfile()
    }

    fun navigateToSignupPetInfo() {
        navController.navigateToSignupPetInfo()
    }

    fun navigateToSignupPetStyle() {
        navController.navigateToSignupPetStyle()
    }

    fun navigateToSignupComplete() {
        navController.navigateToSignupComplete()
    }


    fun navigationToMainTab(
        mainTabDataModel: MainTabDataModel = MainTabDataModel.Walk
    ) {
        navController.navigate(
            route = RouteModel.MainTab(mainTabDataModel)
        ){
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}

