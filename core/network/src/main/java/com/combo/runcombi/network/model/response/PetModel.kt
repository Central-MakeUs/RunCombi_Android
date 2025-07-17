package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PetModel(
    val age: Int? = null,
    val name: String? = null,
    val petId: Int? = null,
    val petImageKey: String? = null,
    val petImageUrl: String? = null,
    val runStyle: String? = null,
    val weight: Double? = null,
)