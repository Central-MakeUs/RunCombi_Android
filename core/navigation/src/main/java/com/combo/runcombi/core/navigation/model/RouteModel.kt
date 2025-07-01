package com.combo.runcombi.core.navigation.model

import kotlinx.serialization.Serializable

sealed interface RouteModel {
    @Serializable
    data object Login : RouteModel

    @Serializable
    data object Onboarding : RouteModel
}

fun RouteModel.fullPathName(): String? {
    return this::class.qualifiedName
}
