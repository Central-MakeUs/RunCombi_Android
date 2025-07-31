package com.combo.runcombi.walk.model

sealed interface WalkResultEvent {
    data object RequestCameraPermission : WalkResultEvent
    data object OpenCamera : WalkResultEvent
    data class PermissionDenied(val type: PermissionType) : WalkResultEvent
    data object SetRunImageSuccess : WalkResultEvent
    data class SaveRunError(val message: String): WalkResultEvent
}

enum class PermissionType {
    CAMERA
}