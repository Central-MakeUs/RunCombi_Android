package com.combo.runcombi.setting.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.setting.component.InputStepScaffold
import com.combo.runcombi.setting.screen.AddPetInfoScreen
import com.combo.runcombi.setting.screen.AddPetProfileScreen
import com.combo.runcombi.setting.screen.AddPetStyleScreen
import com.combo.runcombi.setting.screen.EditMemberScreen
import com.combo.runcombi.setting.screen.EditPetScreen
import com.combo.runcombi.setting.screen.MyScreen
import com.combo.runcombi.setting.screen.SettingScreen
import com.combo.runcombi.setting.screen.UserWithdrawalInfo
import com.combo.runcombi.setting.screen.UserWithdrawalSurvey
import com.combo.runcombi.setting.viewmodel.AccountDeletionViewModel
import com.combo.runcombi.setting.viewmodel.AddPetViewModel

fun NavController.navigateToSettingMain(
    navOptions: NavOptions? = androidx.navigation.navOptions {
        popUpTo(this@navigateToSettingMain.graph.id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    },
) {
    this.navigate(MainTabDataModel.Setting, navOptions)
}

fun NavController.navigateToSetting() {
    this.navigate(RouteModel.MainTabRoute.SettingRouteModel.Setting)
}

fun NavController.navigateToEditMember() {
    this.navigate(RouteModel.MainTabRoute.SettingRouteModel.EditMember)
}

fun NavController.navigateToEditPet(petId: Int) {
    this.navigate(RouteModel.MainTabRoute.SettingRouteModel.EditPet(petId = petId))
}

fun NavController.navigateToAddPet() {
    this.navigate(RouteModel.MainTabRoute.SettingRouteModel.PetInput)
}

fun NavController.navigateToAddPetInfo() {
    this.navigate(RouteModel.MainTabRoute.SettingRouteModel.PetInputRoute.PetInfo)
}

fun NavController.navigateToAddPetStyle() {
    this.navigate(RouteModel.MainTabRoute.SettingRouteModel.PetInputRoute.PetStyle)
}

fun NavController.navigateToAccountDeletion() {
    this.navigate(RouteModel.MainTabRoute.SettingRouteModel.AccountDeletion)
}

fun NavController.navigateToAccountDeletionSurvey() {
    this.navigate(RouteModel.MainTabRoute.SettingRouteModel.AccountDeletionRoute.AccountDeletionSurvey)
}

fun NavGraphBuilder.settingNavGraph(
    navController: NavController,
    onClickSetting: () -> Unit = {},
    onClickAddPet: () -> Unit = {},
    onClickEditMember: () -> Unit = {},
    onClickEditPet: (petId: Int) -> Unit = {},
    goToLogin: () -> Unit = {},
    onBack: () -> Unit = {},
    onNavigateToAddPetInfo: () -> Unit = {},
    onNavigateToAddPetStyle: () -> Unit = {},
    onAddPetSuccess: () -> Unit = {},
    onClickAccountDeletion: () -> Unit = {},
    onNavigateToSurvey: () -> Unit = {},
) {
    navigation<MainTabDataModel.Setting>(
        startDestination = RouteModel.MainTabRoute.SettingRouteModel.My,
    ) {
        composable<RouteModel.MainTabRoute.SettingRouteModel.My> {
            MyScreen(
                onClickSetting = onClickSetting,
                onClickAddPet = onClickAddPet,
                onClickEditPet = onClickEditPet,
                onClickEditMember = onClickEditMember,
            )
        }

        composable<RouteModel.MainTabRoute.SettingRouteModel.Setting> {
            SettingScreen(
                goToLogin = goToLogin,
                onBack = onBack,
                onClickAccountDeletion = onClickAccountDeletion
            )
        }

        composable<RouteModel.MainTabRoute.SettingRouteModel.EditMember> {
            EditMemberScreen(
                onBack = onBack
            )
        }

        composable<RouteModel.MainTabRoute.SettingRouteModel.EditPet> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteModel.MainTabRoute.SettingRouteModel.EditPet>()

            EditPetScreen(
                petId = route.petId,
                onBack = onBack
            )
        }

        addPetNavGraph(
            navController = navController,
            onBack = onBack,
            onSuccess = onAddPetSuccess,
            onNavigateToAddPetInfo = onNavigateToAddPetInfo,
            onNavigateToAddPetStyle = onNavigateToAddPetStyle
        )

        accountDeletionNavGraph(
            navController = navController,
            onBack = onBack,
            onNavigateToSurvey = onNavigateToSurvey,
            onSuccess = goToLogin
        )
    }
}

fun NavGraphBuilder.addPetNavGraph(
    navController: NavController,
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onNavigateToAddPetInfo: () -> Unit = {},
    onNavigateToAddPetStyle: () -> Unit = {},
) {
    navigation<RouteModel.MainTabRoute.SettingRouteModel.PetInput>(
        startDestination = RouteModel.MainTabRoute.SettingRouteModel.PetInputRoute.PetProfile,
    ) {
        composable<RouteModel.MainTabRoute.SettingRouteModel.PetInputRoute.PetProfile> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.MainTabRoute.SettingRouteModel.PetInput>()
            }

            val addPetViewModel = hiltViewModel<AddPetViewModel>(parentEntry)


            InputStepScaffold(currentStep = 0, title = "반려견 정보", onBack = onBack) {
                AddPetProfileScreen(
                    onNext = onNavigateToAddPetInfo,
                    addPetViewModel = addPetViewModel
                )
            }
        }

        composable<RouteModel.MainTabRoute.SettingRouteModel.PetInputRoute.PetInfo> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.MainTabRoute.SettingRouteModel.PetInput>()
            }

            val addPetViewModel = hiltViewModel<AddPetViewModel>(parentEntry)

            InputStepScaffold(currentStep = 1, title = "반려견 정보", onBack = onBack) {
                AddPetInfoScreen(
                    onNext = onNavigateToAddPetStyle,
                    addPetViewModel = addPetViewModel
                )
            }
        }

        composable<RouteModel.MainTabRoute.SettingRouteModel.PetInputRoute.PetStyle> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.MainTabRoute.SettingRouteModel.PetInput>()
            }

            val addPetViewModel = hiltViewModel<AddPetViewModel>(parentEntry)


            InputStepScaffold(currentStep = 2, title = "반려견 정보", onBack = onBack) {
                AddPetStyleScreen(
                    onSuccess = onSuccess,
                    addPetViewModel = addPetViewModel
                )
            }
        }
    }
}

fun NavGraphBuilder.accountDeletionNavGraph(
    navController: NavController,
    onBack: () -> Unit = {},
    onNavigateToSurvey: () -> Unit = {},
    onSuccess: () -> Unit = {},
) {
    navigation<RouteModel.MainTabRoute.SettingRouteModel.AccountDeletion>(
        startDestination = RouteModel.MainTabRoute.SettingRouteModel.AccountDeletionRoute.AccountDeletionInfo,
    ) {
        composable<RouteModel.MainTabRoute.SettingRouteModel.AccountDeletionRoute.AccountDeletionInfo> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.MainTabRoute.SettingRouteModel.AccountDeletion>()
            }

            val accountDeletionViewModel = hiltViewModel<AccountDeletionViewModel>(parentEntry)

            UserWithdrawalInfo(
                onBack = onBack,
                onClickNext = onNavigateToSurvey,
                accountDeletionViewModel = accountDeletionViewModel
            )
        }

        composable<RouteModel.MainTabRoute.SettingRouteModel.AccountDeletionRoute.AccountDeletionSurvey> {
            val parentEntry = remember(it) {
                navController.getBackStackEntry<RouteModel.MainTabRoute.SettingRouteModel.AccountDeletion>()
            }

            val accountDeletionViewModel = hiltViewModel<AccountDeletionViewModel>(parentEntry)

            UserWithdrawalSurvey(
                onBack = onBack,
                onComplete = onSuccess,
                accountDeletionViewModel = accountDeletionViewModel
            )
        }

    }
}
