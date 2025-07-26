package com.combo.runcombi.history.model

data class RunData(
    val memo: String,
    val petData: List<PetRunData>,
    val regDate: String,
    val routeImageUrl: String,
    val runDistance: Double,
    val runEvaluating: ExerciseRating,
    val runId: Int,
    val runImageUrl: String,
    val runTime: Int,
)

