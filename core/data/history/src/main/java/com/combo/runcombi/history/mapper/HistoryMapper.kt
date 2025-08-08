package com.combo.runcombi.history.mapper

import com.combo.runcombi.history.model.DayHistory
import com.combo.runcombi.history.model.ExerciseRating
import com.combo.runcombi.history.model.MonthHistory
import com.combo.runcombi.history.model.PetRunData
import com.combo.runcombi.history.model.RunData
import com.combo.runcombi.history.model.RunDate
import com.combo.runcombi.network.model.response.DayHistoryResponse
import com.combo.runcombi.network.model.response.DayHistoryResult
import com.combo.runcombi.network.model.response.HistoryResponse
import com.combo.runcombi.network.model.response.MonthData
import com.combo.runcombi.network.model.response.MonthHistoryResponse
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
            runTime = runTime ?: 0,
            nickname = nickname ?: "",
            profileImgUrl = profileImgUrl ?: "",
            memberCal = memberCal ?: 0
        )
    }
}

fun String?.toExerciseRatingOrDefault(default: ExerciseRating = ExerciseRating.NORMAL): ExerciseRating {
    return ExerciseRating.entries.firstOrNull { it.name == this } ?: default
}

fun PetData.toDomainModel(): PetRunData {
    return PetRunData(name ?: "", petCal ?: 0, petId ?: 0, petImageUrl ?: "")
}

fun MonthHistoryResponse.toDomainModel(): MonthHistory {
    with(result) {
        return MonthHistory(
            avgCal = avgCal ?: 0,
            avgTime = avgTime ?: 0,
            avgDistance = avgDistance ?: 0.0,
            monthData = monthData?.map { it.toDomainModel() } ?: emptyList(),
            mostRunStyle = mostRunStyle ?: ""
        )
    }
}

fun MonthData.toDomainModel(): RunDate {
    return RunDate(
        date = date ?: "",
        runId = runId ?: emptyList()
    )
}

fun DayHistoryResponse.toDomainModel(): List<DayHistory> {
    return result.map { it.toDomainModel() }
}


fun DayHistoryResult.toDomainModel(): DayHistory {
    return DayHistory(
        regDate = regDate ?: "",
        runDistance = runDistance ?: 0.0,
        runId = runId ?: 0,
        runImageUrl = runImageUrl ?: "",
        runTime = runTime ?: 0
    )
}


