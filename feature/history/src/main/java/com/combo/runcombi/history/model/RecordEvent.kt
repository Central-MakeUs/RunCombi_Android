package com.combo.runcombi.history.model

sealed interface RecordEvent {
    data class Error(val errorMessage: String): RecordEvent
    data object DeleteSuccess: RecordEvent
}