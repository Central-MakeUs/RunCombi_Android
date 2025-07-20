package com.combo.runcombi.walk.model

import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet

data class WalkData(
    val member: Member? = null,
    val petList: List<Pet> = emptyList(),
    val exerciseType: ExerciseType = ExerciseType.WALKING,
    val time: Int = 0,
    val distance: Double = 0.0,
    val pathPoints: List<com.google.android.gms.maps.model.LatLng> = emptyList(),
    val runId: Int? = null
) 