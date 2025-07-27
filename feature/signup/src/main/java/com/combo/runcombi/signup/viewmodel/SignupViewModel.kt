package com.combo.runcombi.signup.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.signup.model.BodyData
import com.combo.runcombi.signup.model.GenderData
import com.combo.runcombi.signup.model.PetInfoData
import com.combo.runcombi.signup.model.PetProfileData
import com.combo.runcombi.signup.model.PetStyleData
import com.combo.runcombi.signup.model.ProfileData
import com.combo.runcombi.signup.model.SignupData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignupViewModel :
    ViewModel() {
    private val _signupData = MutableStateFlow(SignupData())
    val signupData: StateFlow<SignupData> = _signupData.asStateFlow()

    fun setProfile(data: ProfileData) {
        _signupData.value = _signupData.value.copy(profileData = data)
    }

    fun setGender(data: GenderData) {
        _signupData.value = _signupData.value.copy(genderData = data)
    }

    fun setBody(data: BodyData) {
        _signupData.value = _signupData.value.copy(bodyData = data)
    }

    fun setPetProfile(data: PetProfileData) {
        _signupData.value = _signupData.value.copy(petProfileData = data)
    }

    fun setPetInfo(data: PetInfoData) {
        _signupData.value = _signupData.value.copy(petInfoData = data)
    }

    fun setPetStyle(data: PetStyleData) {
        _signupData.value = _signupData.value.copy(petStyleData = data)
    }

    fun getSignupData(): SignupData = _signupData.value

    fun clearProfile() {
        _signupData.value = _signupData.value.copy(profileData = ProfileData())
    }

    fun clearGender() {
        _signupData.value = _signupData.value.copy(genderData = GenderData())
    }

    fun clearBody() {
        _signupData.value = _signupData.value.copy(bodyData = BodyData())
    }

    fun clearPetProfile() {
        _signupData.value = _signupData.value.copy(petProfileData = PetProfileData())
    }

    fun clearPetInfo() {
        _signupData.value = _signupData.value.copy(petInfoData = PetInfoData())
    }

    fun clearPetStyle() {
        _signupData.value = _signupData.value.copy(petStyleData = PetStyleData())
    }
}