package com.combo.runcombi.walk.model

import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.google.android.gms.maps.model.LatLng

data class WalkData(
    val member: WalkMemberUiModel? = null,
    val petList: List<WalkPetUIModel> = emptyList(),
    val exerciseType: ExerciseType = ExerciseType.WALKING,
    val time: Int = 0,
    val distance: Double = 0.0,
    val pathPoints: List<LatLng> = emptyList(),
    val runData: StartRunData? = null

) 