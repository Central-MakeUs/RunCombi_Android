package com.combo.runcombi.setting.model

import com.combo.runcombi.domain.user.model.RunStyle
import java.io.File

data class AddPetData(
    val profileData: PetProfileData = PetProfileData(),
    val infoData: PetInfoData = PetInfoData(),
    val styleData: PetStyleData = PetStyleData()
)

data class PetProfileData(
    val profileFile: File? = null,
    val name: String = ""
)

data class PetInfoData(
    val petAge: Int? = null,
    val petWeight: Double? = null
)

data class PetStyleData(
    val walkStyle: RunStyle = RunStyle.RUNNING
) 