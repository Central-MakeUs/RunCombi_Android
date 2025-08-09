package com.combo.runcombi.history.model

import com.combo.runcombi.walk.model.ExerciseType

data class EditRecordUiState(
    val startDateTime: String = "",
    val distance: Double? = null,
    val time: Int? = null,
    val exerciseType: ExerciseType? = null,
    val isLoading: Boolean = false,
)
