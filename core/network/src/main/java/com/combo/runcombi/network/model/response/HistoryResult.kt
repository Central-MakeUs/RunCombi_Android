package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HistoryResult(
    val memo: String?,
    val petData: List<PetData>?,
    val regDate: String?,
    val routeImageUrl: String?,
    val runDistance: Double?,
    val runEvaluating: String?,
    val runId: Int?,
    val runImageUrl: String?,
    val runTime: Int?
)