package com.combo.runcombi.signup.model

data class SignupData(
    val profile: ProfileData = ProfileData(),
    val gender: GenderData = GenderData(),
    val body: BodyData = BodyData(),
    val petProfile: PetProfileData = PetProfileData(),
    val petInfo: PetInfoData = PetInfoData(),
    val petStyle: PetStyleData = PetStyleData(),
) 