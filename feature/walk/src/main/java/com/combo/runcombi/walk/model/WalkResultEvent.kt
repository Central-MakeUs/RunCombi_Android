package com.combo.runcombi.walk.model

sealed interface WalkResultEvent {
    data object RequestCameraPermission : WalkResultEvent
    data object OpenCamera : WalkResultEvent
    data class PermissionDenied(val type: PermissionType) : WalkResultEvent
    data object Success : WalkResultEvent
}

enum class PermissionType {
    CAMERA
}