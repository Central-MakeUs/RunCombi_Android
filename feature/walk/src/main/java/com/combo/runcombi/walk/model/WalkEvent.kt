package com.combo.runcombi.walk.model

sealed interface WalkEvent {
    data class Error(val message: String) : WalkEvent
    object RequestLocationPermission : WalkEvent
}