package com.combo.runcombi.history.model

import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.walk.model.ExerciseType

data class AddRecordUiState(
    val startDateTime: String = "",
    val distance: Double? = null,
    val time: Int? = null,
    val exerciseType: ExerciseType? = null,
    val member: Member? = null,
    val petList: List<PetUiModel> = emptyList(),
    val isLoading: Boolean = false,
)

data class PetUiModel(
    val pet: Pet,
    val isSelected: Boolean = false,
    val originIndex: Int,
    val selectedOrder: Int? = null, // 선택 순서(없으면 미선택)
)
