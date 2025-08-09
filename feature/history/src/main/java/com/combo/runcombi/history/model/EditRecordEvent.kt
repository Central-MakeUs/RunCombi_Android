package com.combo.runcombi.history.model

sealed interface EditRecordEvent {
    data class Error(val errorMessage: String) : EditRecordEvent
    data object EditSuccess : EditRecordEvent
}
