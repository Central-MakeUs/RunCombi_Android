package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DeleteDataResult(
    val resultPetName: List<String>?,
    val resultRun: Int?,
    val resultRunImage: Int?
)