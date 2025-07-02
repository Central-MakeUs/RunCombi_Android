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
        data object Complete : SignupRoute

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
            data object PetInfo : InputRoute

            @Serializable
            data object PetStyle : InputRoute
        }
    }
}

fun RouteModel.fullPathName(): String? {
    return this::class.qualifiedName
}
