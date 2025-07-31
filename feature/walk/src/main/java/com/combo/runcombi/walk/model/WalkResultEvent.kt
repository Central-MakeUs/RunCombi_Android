package com.combo.runcombi.walk.model

sealed interface WalkResultEvent {
    data object OpenCamera : WalkResultEvent
    data object SetRunImageSuccess : WalkResultEvent
    data class SaveRunError(val message: String): WalkResultEvent
}