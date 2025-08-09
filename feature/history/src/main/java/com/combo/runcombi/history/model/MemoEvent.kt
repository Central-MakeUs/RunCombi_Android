package com.combo.runcombi.history.model

sealed interface MemoEvent {
    data class Error(val errorMessage: String) : MemoEvent
    data object MemoSuccess : MemoEvent
}
