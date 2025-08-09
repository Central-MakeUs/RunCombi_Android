package com.combo.runcombi.history.model

sealed interface AddRecordEvent {
    data class Error(val errorMessage: String) : AddRecordEvent
    data object AddSuccess : AddRecordEvent
}
