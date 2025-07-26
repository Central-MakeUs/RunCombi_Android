package com.combo.runcombi.history.model

data class PetCalUi(
    val petCal: Int = 0,
    val petImageUrl: String = "",
)

data class RecordUiState(
    val runTime: Int = 0,
    val runDistance: Double = 0.0,
    val memberCal: Int = 0,
    val memberImageUrl: String = "",
    val petCalList: List<PetCalUi> = emptyList(),
    val imagePaths: List<String> = emptyList(),
    val selectedRating: ExerciseRating = ExerciseRating.SO_EASY,
    val memo: String = "",
    val isLoading: Boolean = false,
)