package com.combo.runcombi.signup.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.signup.SignupViewModel
import com.combo.runcombi.signup.component.SignupInputStepScaffold
import com.combo.runcombi.signup.screen.BodyScreen
import com.combo.runcombi.signup.screen.CompleteScreen
import com.combo.runcombi.signup.screen.GenderScreen
import com.combo.runcombi.signup.screen.PetInfoScreen
import com.combo.runcombi.signup.screen.PetStyleScreen
import com.combo.runcombi.signup.screen.ProfileScreen
import com.combo.runcombi.signup.screen.TermsScreen

fun NavController.navigateToSignup(
    navOptions: NavOptions? = navOptions {
        popUpTo(this@navigateToSignup.graph.id) { inclusive = true }
    },
) {
    this.navigate(RouteModel.Signup, navOptions)
}

fun NavController.navigateToSignupInput() {
    this.navigate(RouteModel.SignupRoute.Input)
}

fun NavController.navigateToSignupGender() {
    this.navigate(RouteModel.SignupRoute.InputRoute.Gender)
}

fun NavController.navigateToSignupBody() {
    this.navigate(RouteModel.SignupRoute.InputRoute.Body)
}

fun NavController.navigateToSignupPetInfo() {
    this.navigate(RouteModel.SignupRoute.InputRoute.PetInfo)
}

fun NavController.navigateToSignupPetStyle() {
    this.navigate(RouteModel.SignupRoute.InputRoute.PetStyle)
}

fun NavController.navigateToSignupComplete(
    navOptions: NavOptions? = navOptions {
        popUpTo(this@navigateToSignupComplete.graph.id) { inclusive = true }
    },
) {
    this.navigate(RouteModel.SignupRoute.Complete, navOptions)
}

fun NavGraphBuilder.signupNavGraph(
    onTermsNext: () -> Unit,
    onProfileNext: () -> Unit,
    onGenderNext: () -> Unit,
    onBodyNext: () -> Unit,
    onPetInfoNext: () -> Unit,
    onPetStyleSuccess: () -> Unit,
    onSignupComplete: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    navigation<RouteModel.Signup>(
        startDestination = RouteModel.SignupRoute.Terms,
    ) {
        composable<RouteModel.SignupRoute.Terms> {
            TermsScreen(
                onNext = onTermsNext,
                viewModel = hiltViewModel()
            )
        }
        inputNavGraph(
            onProfileNext = onProfileNext,
            onGenderNext = onGenderNext,
            onBodyNext = onBodyNext,
            onPetInfoNext = onPetInfoNext,
            onPetStyleSuccess = onPetStyleSuccess,
            onBack = onBack,
        )

        composable<RouteModel.SignupRoute.Complete> {
            CompleteScreen(
                onDone = onSignupComplete,
                viewModel = hiltViewModel()
            )
        }
    }
}

fun NavGraphBuilder.inputNavGraph(
    onProfileNext: () -> Unit,
    onGenderNext: () -> Unit,
    onBodyNext: () -> Unit,
    onPetInfoNext: () -> Unit,
    onPetStyleSuccess: () -> Unit,
    onBack: () -> Unit = {},
) {
    navigation<RouteModel.SignupRoute.Input>(
        startDestination = RouteModel.SignupRoute.InputRoute.Profile,
    ) {
        composable<RouteModel.SignupRoute.InputRoute.Profile> {
            val viewModel: SignupViewModel = hiltViewModel()
            SignupInputStepScaffold(currentStep = 0, onBack = onBack) {
                ProfileScreen(
                    onNext = onProfileNext,
                    viewModel = viewModel
                )
            }
        }
        composable<RouteModel.SignupRoute.InputRoute.Gender> {
            val viewModel: SignupViewModel = hiltViewModel()
            SignupInputStepScaffold(currentStep = 1, onBack = onBack) {
                GenderScreen(
                    onNext = onGenderNext,
                    viewModel = viewModel
                )
            }
        }
        composable<RouteModel.SignupRoute.InputRoute.Body> {
            val viewModel: SignupViewModel = hiltViewModel()
            SignupInputStepScaffold(currentStep = 2, onBack = onBack) {
                BodyScreen(
                    onNext = onBodyNext,
                    viewModel = viewModel
                )
            }
        }
        composable<RouteModel.SignupRoute.InputRoute.PetInfo> {
            val viewModel: SignupViewModel = hiltViewModel()
            SignupInputStepScaffold(currentStep = 3, onBack = onBack) {
                PetInfoScreen(
                    onNext = onPetInfoNext,
                    viewModel = viewModel
                )
            }
        }
        composable<RouteModel.SignupRoute.InputRoute.PetStyle> {
            val viewModel: SignupViewModel = hiltViewModel()
            SignupInputStepScaffold(currentStep = 4, onBack = onBack) {
                PetStyleScreen(
                    onSuccess = onPetStyleSuccess,
                    viewModel = viewModel
                )
            }
        }
    }
}