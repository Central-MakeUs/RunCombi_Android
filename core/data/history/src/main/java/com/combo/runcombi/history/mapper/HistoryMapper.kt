package com.combo.runcombi.history.mapper

import com.combo.runcombi.history.model.ExerciseRating
import com.combo.runcombi.history.model.PetRunData
import com.combo.runcombi.history.model.RunData
import com.combo.runcombi.network.model.response.HistoryResponse
import com.combo.runcombi.network.model.response.PetData

fun HistoryResponse.toDomainModel(): RunData {
    return with(result) {
        RunData(
            memo = memo ?: "",
            petData = petData?.map { it.toDomainModel() } ?: emptyList(),
            regDate = regDate ?: "",
            routeImageUrl = routeImageUrl ?: "",
            runDistance = runDistance ?: 0.0,
            runEvaluating = runEvaluating.toExerciseRatingOrDefault(),
            runId = runId ?: 0,
            runImageUrl = runImageUrl ?: "",
            runTime = runTime ?: 0
        )
    }
}

fun String?.toExerciseRatingOrDefault(default: ExerciseRating = ExerciseRating.NORMAL): ExerciseRating {
    return ExerciseRating.entries.firstOrNull { it.name == this } ?: default
}

fun PetData.toDomainModel(): PetRunData {
    return PetRunData(name ?: "", petCal ?: 0, petId ?: 0, petImageUrl ?: "")
}



