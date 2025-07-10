package com.combo.runcombi.walk.model

import com.google.android.gms.maps.model.LatLng

data class WalkMainUiState(
    val myLocation: LatLng? = null,
    val address: String = "",
    val isLoading: Boolean = false
)