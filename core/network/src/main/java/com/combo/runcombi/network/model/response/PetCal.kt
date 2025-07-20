package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PetCal(
    val petId: Int = 0,
    val petCal: Int = 0,
)
