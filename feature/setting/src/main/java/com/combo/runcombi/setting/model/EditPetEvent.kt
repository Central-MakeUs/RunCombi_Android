package com.combo.runcombi.setting.model

sealed interface EditPetEvent {
    data class Error(val errorMessage: String) : EditPetEvent
    data object Success : EditPetEvent
}