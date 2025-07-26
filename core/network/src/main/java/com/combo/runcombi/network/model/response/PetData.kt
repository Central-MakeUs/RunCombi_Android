package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PetData(
    val name: String?,
    val petCal: Int?,
    val petId: Int?,
    val petImageUrl: String?
)