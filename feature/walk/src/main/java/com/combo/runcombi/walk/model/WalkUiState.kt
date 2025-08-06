package com.combo.runcombi.walk.model

import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.google.android.gms.maps.model.LatLng

data class WalkUiState(
    val time: Int = 0,
    val distance: Double = 0.0,
    val isPaused: Boolean = false,
    val pathPoints: List<LatLng> = emptyList(),
    val walkMemberUiModel: WalkMemberUiModel? = null,
    val walkPetUIModelList: List<WalkPetUIModel>? = null,
    val exerciseType: ExerciseType = ExerciseType.WALKING
)

data class WalkMemberUiModel(val member: Member, val calorie: Int = 0)
data class WalkPetUIModel(val pet: Pet, val calorie: Int = 0)