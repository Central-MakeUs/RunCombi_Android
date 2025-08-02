package com.combo.runcombi.setting.model

sealed class AddPetEvent {
    data class Success(val message: String) : AddPetEvent()
    data class Error(val errorMessage: String) : AddPetEvent()
} 