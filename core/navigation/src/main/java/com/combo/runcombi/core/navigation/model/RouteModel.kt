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

    @Serializable
    sealed interface MainTabRoute : RouteModel {
        @Serializable
        sealed interface WalkRouteModel : MainTabRoute {
            @Serializable
            data object Walk : WalkRouteModel

            @Serializable
            data object WalkTracking : WalkRouteModel

            @Serializable
            data object WalkResult : WalkRouteModel
        }

        @Serializable
        sealed interface HistoryRouteModel : MainTabRoute {
            @Serializable
            data object History : HistoryRouteModel
        }

        @Serializable
        sealed interface SettingRouteModel : MainTabRoute {
            @Serializable
            data object Setting : SettingRouteModel
        }
    }
}

fun RouteModel.fullPathName(): String? {
    return this::class.qualifiedName
}
