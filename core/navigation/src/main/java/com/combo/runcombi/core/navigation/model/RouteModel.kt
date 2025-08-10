package com.combo.runcombi.core.navigation.model

import kotlinx.serialization.Serializable

sealed interface RouteModel {
    @Serializable
    data object Login : RouteModel

    @Serializable
    data object Signup : RouteModel

    @Serializable
    sealed interface SignupRoute : RouteModel {
        @Serializable
        data object Terms : SignupRoute

        @Serializable
        data class Complete(val userName: String, val petName: String) : SignupRoute

        @Serializable
        data object Input : SignupRoute

        @Serializable
        sealed interface InputRoute : SignupRoute {
            @Serializable
            data object Profile : InputRoute

            @Serializable
            data object Gender : InputRoute

            @Serializable
            data object Body : InputRoute

            @Serializable
            data object PetProfile : InputRoute

            @Serializable
            data object PetInfo : InputRoute

            @Serializable
            data object PetStyle : InputRoute
        }
    }

    @Serializable
    data class MainTab(
        val mainTabDataModel: MainTabDataModel = MainTabDataModel.None,
    ) : RouteModel

    sealed interface MainTabRoute : RouteModel {
        @Serializable
        sealed interface WalkRouteModel : MainTabRoute {

            @Serializable
            data object WalkMain : WalkRouteModel

            @Serializable
            data object WalkCountdown : WalkRouteModel

            @Serializable
            data object WalkTypeSelect : WalkRouteModel

            @Serializable
            data object WalkReady : WalkRouteModel


            @Serializable
            data object Walk : WalkRouteModel

            @Serializable
            sealed interface WalkRoute : WalkRouteModel {
                @Serializable
                data object WalkTracking : WalkRoute

                @Serializable
                data object WalkResult : WalkRoute
            }
        }

        @Serializable
        sealed interface HistoryRouteModel : MainTabRoute {
            @Serializable
            data object History : HistoryRouteModel

            @Serializable
            data class Record(val runId: Int) : HistoryRouteModel

            @Serializable
            data class AddRecord(val date: String) : HistoryRouteModel

            @Serializable
            data class EditRecord(val runId: Int) : HistoryRouteModel

            @Serializable
            data class Memo(val runId: Int, val memo: String) : HistoryRouteModel
        }

        @Serializable
        sealed interface SettingRouteModel : MainTabRoute {
            @Serializable
            data object My : SettingRouteModel

            @Serializable
            data object Setting : SettingRouteModel

            @Serializable
            data object EditMember : SettingRouteModel

            @Serializable
            data class EditPet(val petId: Int) : SettingRouteModel

            @Serializable
            data object PetInput : SettingRouteModel

            @Serializable
            sealed interface PetInputRoute : SettingRouteModel {

                @Serializable
                data object PetProfile : PetInputRoute

                @Serializable
                data object PetInfo : PetInputRoute

                @Serializable
                data object PetStyle : PetInputRoute
            }

            @Serializable
            data object AccountDeletion : SettingRouteModel

            @Serializable
            sealed interface AccountDeletionRoute : SettingRouteModel {

                @Serializable
                data object AccountDeletionInfo : AccountDeletionRoute

                @Serializable
                data object AccountDeletionSurvey : AccountDeletionRoute

            }
        }
    }
}

fun RouteModel.fullPathName(): String? {
    return this::class.qualifiedName
}
