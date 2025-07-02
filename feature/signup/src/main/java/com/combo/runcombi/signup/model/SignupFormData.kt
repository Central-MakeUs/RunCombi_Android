package com.combo.runcombi.signup.model

import com.combo.runcombi.signup.model.TermsData
import com.combo.runcombi.signup.model.ProfileData
import com.combo.runcombi.signup.model.GenderData
import com.combo.runcombi.signup.model.BodyData
import com.combo.runcombi.signup.model.PetInfoData
import com.combo.runcombi.signup.model.PetStyleData

data class SignupFormData(
    val terms: TermsData = TermsData(),
    val profile: ProfileData = ProfileData(),
    val gender: GenderData = GenderData(),
    val body: BodyData = BodyData(),
    val petInfo: PetInfoData = PetInfoData(),
    val petStyle: PetStyleData = PetStyleData()
) 