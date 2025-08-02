package com.combo.runcombi.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.setting.screen.EditMemberScreen
import com.combo.runcombi.setting.screen.EditPetScreen
import com.combo.runcombi.setting.screen.MyScreen
import com.combo.runcombi.setting.screen.SettingScreen

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

fun NavGraphBuilder.settingNavGraph(
    onClickSetting: () -> Unit = {},
    onClickAddPet: () -> Unit = {},
    onClickEditMember: () -> Unit = {},
    onClickEditPet: (petId: Int) -> Unit = {},
    goToLogin: () -> Unit = {},
    onBack: () -> Unit = {},
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
                onBack = onBack
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
    }
}
