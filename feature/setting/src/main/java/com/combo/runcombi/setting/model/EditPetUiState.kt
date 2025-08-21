package com.combo.runcombi.setting.model

import com.combo.runcombi.domain.user.model.RunStyle


data class EditPetUiState(
    val name: String = "",
    val profileImageUrl: String = "",
    val age: String = "",
    val weight: String = "",
    val runStyle: RunStyle = RunStyle.RUNNING,
    val isNameError: Boolean = false,
    val isAgeError: Boolean = false,
    val isWeightError: Boolean = false,
    val isLoading: Boolean = false,
    val isRemovable: Boolean = true,
    val hasChanges: Boolean = false,
)