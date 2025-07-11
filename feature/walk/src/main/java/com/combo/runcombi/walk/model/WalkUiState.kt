package com.combo.runcombi.walk.model

import com.google.android.gms.maps.model.LatLng

data class WalkUiState(
    val time: Long = 0L,
    val distance: Double = 0.0,
    val speed: Double = 0.0,
    val calorie: Double = 0.0,
    val isPaused: Boolean = false,
    val pathPoints: List<LatLng> = emptyList()
)