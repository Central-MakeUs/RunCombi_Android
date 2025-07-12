package com.combo.runcombi.signup.model

import com.combo.runcombi.pet.model.WalkStyle

data class PetStyleUiState(
    val selectedStyle: WalkStyle = WalkStyle.ENERGETIC,
    val isButtonEnabled: Boolean = true,
)