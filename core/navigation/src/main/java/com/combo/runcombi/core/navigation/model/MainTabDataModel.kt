package com.combo.runcombi.core.navigation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
sealed class MainTabDataModel : Parcelable {
    @Serializable
    data object Walk : MainTabDataModel()

    @Serializable
    data object History : MainTabDataModel()

    @Serializable
    data object Setting : MainTabDataModel()

    @Serializable
    data object None : MainTabDataModel()
}
