package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PetRunData(
    val petCalList: List<PetCal>,
)