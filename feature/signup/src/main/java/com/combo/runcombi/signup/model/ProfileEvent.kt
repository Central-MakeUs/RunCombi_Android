package com.combo.runcombi.signup.model

import android.graphics.Bitmap

sealed interface ProfileEvent {
    object ShowImagePickerBottomSheet : ProfileEvent
    object RequestCameraPermission : ProfileEvent
    object OpenAlbum : ProfileEvent
    object OpenCamera : ProfileEvent
    data class PermissionDenied(val type: PermissionType) : ProfileEvent
    object Error : ProfileEvent
}

enum class PermissionType {
    CAMERA
} 