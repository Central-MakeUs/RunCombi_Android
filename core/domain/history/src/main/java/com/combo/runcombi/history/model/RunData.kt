package com.combo.runcombi.history.model

data class RunData(
    val nickname: String,
    val profileImgUrl: String,
    val memberRunStyle: String,
    val memberCal: Int,
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

