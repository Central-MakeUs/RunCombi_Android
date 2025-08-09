package com.combo.runcombi.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.history.navigation.navigateToEditRecord

import com.combo.runcombi.history.navigation.navigateToHistory
import com.combo.runcombi.history.navigation.navigateToMemo
import com.combo.runcombi.history.navigation.navigateToRecord
import com.combo.runcombi.setting.navigation.navigateToAddPet
import com.combo.runcombi.setting.navigation.navigateToAddPetInfo
import com.combo.runcombi.setting.navigation.navigateToAddPetProfile
import com.combo.runcombi.setting.navigation.navigateToAddPetStyle
import com.combo.runcombi.setting.navigation.navigateToEditMember
import com.combo.runcombi.setting.navigation.navigateToEditPet
import com.combo.runcombi.setting.navigation.navigateToSetting
import com.combo.runcombi.setting.navigation.navigateToSettingMain
import com.combo.runcombi.walk.navigation.navigateToWalkCountDown
import com.combo.runcombi.walk.navigation.navigateToWalkMain
import com.combo.runcombi.walk.navigation.navigateToWalkReady
import com.combo.runcombi.walk.navigation.navigateToWalkResult
import com.combo.runcombi.walk.navigation.navigateToWalkTracking
import com.combo.runcombi.walk.navigation.navigateToWalkTypeSelect

class MainTabNavigator(
    val navController: NavHostController,
) {
    fun navigationToHistory() {
        navController.navigateToHistory()
    }

    fun navigationToRecord(runId: Int, navOptions: NavOptions?) {
        navController.navigateToRecord(runId = runId, navOptions = navOptions)
    }

    fun navigationToEditRecord(runId: Int) {
        navController.navigateToEditRecord(runId = runId)
    }

    fun navigationToMemo(runId: Int, memo: String) {
        navController.navigateToMemo(runId = runId, memo = memo)
    }

    fun navigationToSettingMain() {
        navController.navigateToSettingMain()
    }

    fun navigationToSetting() {
        navController.navigateToSetting()
    }

    fun navigationToEditMember() {
        navController.navigateToEditMember()
    }

    fun navigationToEditPet(petId: Int) {
        navController.navigateToEditPet(petId)
    }

    fun navigationToAddPet() {
        navController.navigateToAddPet()
    }

    fun navigationToAddPetInfo() {
        navController.navigateToAddPetInfo()
    }

    fun navigationToAddPetStyle() {
        navController.navigateToAddPetStyle()
    }

    fun navigateToMyScreen() {
        navController.navigateToSettingMain(
            navOptions = androidx.navigation.navOptions {
                popUpTo(RouteModel.MainTabRoute.SettingRouteModel.PetInput) {
                    inclusive = true
                }
            }
        )
    }

    fun navigationToWalkMain() {
        navController.navigateToWalkMain()
    }

    fun navigationToWalkTypeSelect() {
        navController.navigateToWalkTypeSelect()
    }

    fun navigationToWalkReady() {
        navController.navigateToWalkReady()
    }

    fun navigationToWalkCountdown() {
        navController.navigateToWalkCountDown()
    }

    fun navigationToWalkTracking() {
        navController.navigateToWalkTracking()
    }

    fun navigationToWalkResult() {
        navController.navigateToWalkResult()
    }
}

@Composable
internal fun rememberMainTabNavigator(
    navController: NavHostController = rememberNavController(),
): MainTabNavigator = remember(navController) {
    MainTabNavigator(navController)
}

