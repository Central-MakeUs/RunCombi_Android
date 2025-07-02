package com.combo.runcombi.main.navigation

import androidx.navigation.NavDestination
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.core.navigation.model.fullPathName

fun NavDestination?.compareTo(routeModel: RouteModel): Boolean {
    return this?.route == routeModel.fullPathName()
}

