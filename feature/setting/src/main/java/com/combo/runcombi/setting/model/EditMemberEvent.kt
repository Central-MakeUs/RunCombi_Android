package com.combo.runcombi.setting.model

sealed interface EditMemberEvent {
    data class Error(val errorMessage: String) : EditMemberEvent
    data object Success : EditMemberEvent
}