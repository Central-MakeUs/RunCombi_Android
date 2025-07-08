package com.combo.runcombi.core.navigation.model

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val MainTabDataModelType = object : NavType<MainTabDataModel>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): MainTabDataModel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, MainTabDataModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): MainTabDataModel {
        return Json.decodeFromString<MainTabDataModel>(value)
    }

    override fun serializeAsValue(value: MainTabDataModel): String {
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: MainTabDataModel) {
        bundle.putParcelable(key, value)
    }
}
