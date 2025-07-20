package com.combo.runcombi.walk.model

import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet

data class WalkData(
    val member: Member? = null,
    val petList: List<Pet> = emptyList(),
    val exerciseType: ExerciseType = ExerciseType.WALKING
) 