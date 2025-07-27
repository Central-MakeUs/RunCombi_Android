package com.combo.runcombi.signup.model

data class SignupData(
    val profileData: ProfileData = ProfileData(),
    val genderData: GenderData = GenderData(),
    val bodyData: BodyData = BodyData(),
    val petProfileData: PetProfileData = PetProfileData(),
    val petInfoData: PetInfoData = PetInfoData(),
    val petStyleData: PetStyleData = PetStyleData(),
) 