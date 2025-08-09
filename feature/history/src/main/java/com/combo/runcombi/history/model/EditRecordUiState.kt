package com.combo.runcombi.history.model

import com.combo.runcombi.walk.model.ExerciseType

data class EditRecordUiState(
    val startDateTime: String = "",
    val distance: Double = 0.0,
    val time: Int = 0,
    val exerciseType: ExerciseType? = null,
    val isLoading: Boolean = false,
)
