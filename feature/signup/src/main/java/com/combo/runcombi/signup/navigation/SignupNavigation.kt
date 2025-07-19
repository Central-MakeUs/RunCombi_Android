package com.combo.runcombi.signup.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.signup.component.InputStepScaffold
import com.combo.runcombi.signup.screen.BodyScreen
import com.combo.runcombi.signup.screen.CompleteScreen
import com.combo.runcombi.signup.screen.GenderScreen
import com.combo.runcombi.signup.screen.PetInfoScreen
import com.combo.runcombi.signup.screen.PetProfileScreen
import com.combo.runcombi.signup.screen.PetStyleScreen
import com.combo.runcombi.signup.screen.ProfileScreen
import com.combo.runcombi.signup.screen.TermsScreen
import com.combo.runcombi.signup.viewmodel.SignupViewModel

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

fun NavController.navigateToSignupPetProfile() {
    this.navigate(RouteModel.SignupRoute.InputRoute.PetProfile)
}

fun NavController.navigateToSignupPetInfo() {
    this.navigate(RouteModel.SignupRoute.InputRoute.PetInfo)
}

fun NavController.navigateToSignupPetStyle() {
    this.navigate(RouteModel.SignupRoute.InputRoute.PetStyle)
}

fun NavController.navigateToSignupComplete(
    userName: String,
    petName: String,
    navOptions: NavOptions? = navOptions {
        popUpTo(this@navigateToSignupComplete.graph.id) { inclusive = true }
    },
) {
    this.navigate(RouteModel.SignupRoute.Complete(userName, petName), navOptions)
}

fun NavGraphBuilder.signupNavGraph(
    navController: NavController,
    onTermsNext: () -> Unit,
    onProfileNext: () -> Unit,
    onGenderNext: () -> Unit,
    onBodyNext: () -> Unit,
    onPetProfileNext: () -> Unit,
    onPetInfoNext: () -> Unit,
    onPetStyleSuccess: (String, String) -> Unit,
    onSignupComplete: (Boolean) -> Unit = {},
    onBack: () -> Unit = {},
) {
    navigation<RouteModel.Signup>(
        startDestination = RouteModel.SignupRoute.Terms,
    ) {
        composable<RouteModel.SignupRoute.Terms> {
            TermsScreen(
                onNext = onTermsNext
            )
        }
        inputNavGraph(
            navController = navController,
            onProfileNext = onProfileNext,
            onGenderNext = onGenderNext,
            onBodyNext = onBodyNext,
            onPetProfileNext = onPetProfileNext,
            onPetInfoNext = onPetInfoNext,
            onPetStyleSuccess = onPetStyleSuccess,
            onBack = onBack,
        )

        composable<RouteModel.SignupRoute.Complete> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteModel.SignupRoute.Complete>()
            CompleteScreen(
                userName = route.userName,
                petName = route.petName,
                onDone = onSignupComplete
            )
        }
    }
}

fun NavGraphBuilder.inputNavGraph(
    navController: NavController,
    onProfileNext: () -> Unit,
    onGenderNext: () -> Unit,
    onBodyNext: () -> Unit,
    onPetProfileNext: () -> Unit,
    onPetInfoNext: () -> Unit,
    onPetStyleSuccess: (String, String) -> Unit,
    onBack: () -> Unit = {},
) {
    navigation<RouteModel.SignupRoute.Input>(
        startDestination = RouteModel.SignupRoute.InputRoute.Profile,
    ) {
        composable<RouteModel.SignupRoute.InputRoute.Profile> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.SignupRoute.Input>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)


            InputStepScaffold(currentStep = 0, title = "사용자 정보", onBack = onBack) {
                ProfileScreen(
                    onNext = onProfileNext,
                    signupViewModel = signupViewModel
                )
            }
        }

        composable<RouteModel.SignupRoute.InputRoute.Gender> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.SignupRoute.Input>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)


            InputStepScaffold(currentStep = 1, title = "사용자 정보", onBack = onBack) {
                GenderScreen(
                    onNext = onGenderNext,
                    signupViewModel = signupViewModel
                )
            }
        }

        composable<RouteModel.SignupRoute.InputRoute.Body> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.SignupRoute.Input>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)


            InputStepScaffold(currentStep = 2, title = "사용자 정보", onBack = onBack) {
                BodyScreen(
                    onNext = onBodyNext,
                    signupViewModel = signupViewModel
                )
            }
        }

        composable<RouteModel.SignupRoute.InputRoute.PetProfile> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.SignupRoute.Input>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)


            InputStepScaffold(currentStep = 0, title = "반려견 정보", onBack = onBack) {
                PetProfileScreen(
                    onNext = onPetProfileNext,
                    signupViewModel = signupViewModel
                )
            }
        }

        composable<RouteModel.SignupRoute.InputRoute.PetInfo> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.SignupRoute.Input>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)


            InputStepScaffold(currentStep = 1, title = "반려견 정보", onBack = onBack) {
                PetInfoScreen(
                    onNext = onPetInfoNext,
                    signupViewModel = signupViewModel
                )
            }
        }

        composable<RouteModel.SignupRoute.InputRoute.PetStyle> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.SignupRoute.Input>()
            }
            val signupViewModel = hiltViewModel<SignupViewModel>(parentEntry)

            InputStepScaffold(currentStep = 2, title = "반려견 정보", onBack = onBack) {
                PetStyleScreen(
                    onSuccess = onPetStyleSuccess,
                    signupViewModel = signupViewModel
                )
            }
        }
    }
}
