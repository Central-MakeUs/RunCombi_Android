package com.combo.runcombi.walk.model

import com.google.android.gms.maps.model.LatLng

sealed interface WalkEvent {
    object RequestLocationPermission : WalkEvent
    data class LocationUpdated(val latLng: LatLng) : WalkEvent
    data class Error(val message: String) : WalkEvent
}