package com.combo.runcombi.setting.model


sealed interface SuggestionEvent {
    data object Success : SuggestionEvent
    data class Error(val message: String) : SuggestionEvent
}