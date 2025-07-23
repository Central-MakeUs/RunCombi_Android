package com.combo.runcombi.core.navigation.model

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class PetCal(
    val petId: Int,
    val petCal: Int,
) : Parcelable

@Parcelize
@Serializable
data class RecordDataModel(
    val runId: Int,
    val memberCal: Int,
    val runTime: Int,
    val runDistance: Double,
    val petCalList: List<PetCal>,
    val imagePaths: List<String>,
) : Parcelable


val RecordDataModelType = object : NavType<RecordDataModel>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): RecordDataModel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, RecordDataModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): RecordDataModel {
        return Json.decodeFromString<RecordDataModel>(value)
    }

    override fun serializeAsValue(value: RecordDataModel): String {
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: RecordDataModel) {
        bundle.putParcelable(key, value)
    }
}
