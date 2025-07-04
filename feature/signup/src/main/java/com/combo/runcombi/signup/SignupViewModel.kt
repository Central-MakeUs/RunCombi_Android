package com.combo.runcombi.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.signup.model.BodyData
import com.combo.runcombi.signup.model.GenderData
import com.combo.runcombi.signup.model.PetInfoData
import com.combo.runcombi.signup.model.PetProfileData
import com.combo.runcombi.signup.model.PetStyleData
import com.combo.runcombi.signup.model.ProfileData
import com.combo.runcombi.signup.model.SignupFormData
import com.combo.runcombi.signup.model.TermsData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignupViewModel() : ViewModel() {
    private val _signupFormData = MutableStateFlow(SignupFormData())
    val signupFormData: StateFlow<SignupFormData> = _signupFormData.asStateFlow()

    private val _eventFlow = MutableSharedFlow<SignupEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun setTerms(data: TermsData) {
        _signupFormData.value = _signupFormData.value.copy(terms = data)
    }

    fun setProfile(data: ProfileData) {
        _signupFormData.value = _signupFormData.value.copy(profile = data)
    }

    fun setGender(data: GenderData) {
        _signupFormData.value = _signupFormData.value.copy(gender = data)
    }

    fun setBody(data: BodyData) {
        _signupFormData.value = _signupFormData.value.copy(body = data)
    }

    fun setPetProfile(data: PetProfileData) {
        _signupFormData.value = _signupFormData.value.copy(petProfile = data)
    }

    fun setPetInfo(data: PetInfoData) {
        _signupFormData.value = _signupFormData.value.copy(petInfo = data)
    }

    fun setPetStyle(data: PetStyleData) {
        _signupFormData.value = _signupFormData.value.copy(petStyle = data)
    }

    fun getSignupFormData(): SignupFormData = _signupFormData.value


    fun clearProfile() {
        _signupFormData.value = _signupFormData.value.copy(profile = ProfileData())
    }

    fun clearGender() {
        _signupFormData.value = _signupFormData.value.copy(gender = GenderData())
    }

    fun clearBody() {
        _signupFormData.value = _signupFormData.value.copy(body = BodyData())
    }

    fun clearPetProfile() {
        _signupFormData.value = _signupFormData.value.copy(petProfile = PetProfileData())
    }

    fun clearPetInfo() {
        _signupFormData.value = _signupFormData.value.copy(petInfo = PetInfoData())
    }

    fun clearPetStyle() {
        _signupFormData.value = _signupFormData.value.copy(petStyle = PetStyleData())
    }

    fun signup() {
        val data = getSignupFormData()

        // 1. 유효성 검사 예시 (닉네임 필수)
        if (data.profile.nickname.isBlank()) {
            viewModelScope.launch { _eventFlow.emit(SignupEvent.Error) }
            return
        }

        // 2. 서버에 회원가입 요청 (여기선 예시로 성공만 emit)
        viewModelScope.launch {
            // 실제로는 repository.signup(data) 등 호출
            // val result = repository.signup(data)
            // if (result.isSuccess) {
            _eventFlow.emit(SignupEvent.Success)
            // } else {
            //   _eventFlow.emit(SignupEvent.Error)
            // }
        }
    }
}
