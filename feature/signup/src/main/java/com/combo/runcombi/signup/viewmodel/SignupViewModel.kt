package com.combo.runcombi.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.domain.user.usecase.SetUserInfoUseCase
import com.combo.runcombi.signup.model.BodyData
import com.combo.runcombi.signup.model.GenderData
import com.combo.runcombi.signup.model.PetInfoData
import com.combo.runcombi.signup.model.PetProfileData
import com.combo.runcombi.signup.model.PetStyleData
import com.combo.runcombi.signup.model.ProfileData
import com.combo.runcombi.signup.model.SignupData
import com.combo.runcombi.signup.model.SignupEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignupViewModel :
    ViewModel() {
    private val _signupData = MutableStateFlow(SignupData())
    val signupData: StateFlow<SignupData> = _signupData.asStateFlow()

    fun setProfile(data: ProfileData) {
        // data.profileFile에 파일을 넣어야 함
        _signupData.value = _signupData.value.copy(profile = data)
    }

    fun setGender(data: GenderData) {
        _signupData.value = _signupData.value.copy(gender = data)
    }

    fun setBody(data: BodyData) {
        _signupData.value = _signupData.value.copy(body = data)
    }

    fun setPetProfile(data: PetProfileData) {
        // data.profileFile에 파일을 넣어야 함
        _signupData.value = _signupData.value.copy(petProfile = data)
    }

    fun setPetInfo(data: PetInfoData) {
        _signupData.value = _signupData.value.copy(petInfo = data)
    }

    fun setPetStyle(data: PetStyleData) {
        _signupData.value = _signupData.value.copy(petStyle = data)
    }

    fun getSignupData(): SignupData = _signupData.value

    fun clearProfile() {
        _signupData.value = _signupData.value.copy(profile = ProfileData())
    }

    fun clearGender() {
        _signupData.value = _signupData.value.copy(gender = GenderData())
    }

    fun clearBody() {
        _signupData.value = _signupData.value.copy(body = BodyData())
    }

    fun clearPetProfile() {
        _signupData.value = _signupData.value.copy(petProfile = PetProfileData())
    }

    fun clearPetInfo() {
        _signupData.value = _signupData.value.copy(petInfo = PetInfoData())
    }

    fun clearPetStyle() {
        _signupData.value = _signupData.value.copy(petStyle = PetStyleData())
    }
}