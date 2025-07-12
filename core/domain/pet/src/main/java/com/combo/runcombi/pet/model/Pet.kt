package com.combo.runcombi.pet.model

data class Pet(
    val name: String,
    val weight: Double,
    val age: Int,
    val profileImageUrl: String? = null,
    val walkStyle: WalkStyle
)
