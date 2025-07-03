package com.combo.runcombi.main.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.util.setStatusBar
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.feature.login.navigation.loginNavGraph
import com.combo.runcombi.signup.navigation.signupNavGraph

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    startDestination: RouteModel,
    padding: PaddingValues,
) {
    val context = LocalContext.current
    val currentBackStackEntry by navigator.navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination

    LaunchedEffect(currentRoute) {
        setStatusBar(context, false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grey01)
            .systemBarsPadding()
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = startDestination
        ) {
            loginNavGraph(
                onLoginSuccess = {
                    navigator.navigateToSignup()
                }
            )

            signupNavGraph(
                onTermsNext = { navigator.navigateToSignupInput() },
                onProfileNext = { navigator.navigateToSignupGender() },
                onGenderNext = { navigator.navigateToSignupBody() },
                onBodyNext = { navigator.navigateToSignupPetProfile() },
                onPetProfileNext = { navigator.navigateToSignupPetInfo() },
                onPetInfoNext = { navigator.navigateToSignupPetStyle() },
                onPetStyleSuccess = { navigator.navigateToSignupComplete() },
                onSignupComplete = {
                    // TODO: 회원가입 완료 후 처리
                },
                onBack = { navigator.popBackStack() },
            )
        }
    }
}

