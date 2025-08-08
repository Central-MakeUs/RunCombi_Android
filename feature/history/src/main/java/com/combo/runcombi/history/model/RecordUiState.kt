package com.combo.runcombi.history.model

data class PetCalUi(
    val petName: String = "",
    val petCal: Int = 0,
    val petImageUrl: String = "",
)

data class RecordUiState(
    val nickname: String = "",
    val profileImgUrl: String = "",
    val runTime: Int = 0,
    val runDistance: Double = 0.0,
    val date: String = "",
    val memberCal: Int = 0,
    val memberImageUrl: String = "",
    val petCalList: List<PetCalUi> = emptyList(),
    val imagePaths: List<String> = emptyList(),
    val selectedRating: ExerciseRating? = null,
    val memo: String = "",
    val isLoading: Boolean = false,
)