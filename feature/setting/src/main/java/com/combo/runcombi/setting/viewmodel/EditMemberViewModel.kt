package com.combo.runcombi.setting.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.domain.user.usecase.UpdateMemberUseCase
import com.combo.runcombi.setting.model.EditMemberEvent
import com.combo.runcombi.setting.model.EditMemberUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditMemberViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateMemberUseCase: UpdateMemberUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditMemberUiState())
    val uiState: StateFlow<EditMemberUiState> = _uiState

    private val _profileBitmap = MutableStateFlow<Bitmap?>(null)
    val profileBitmap: StateFlow<Bitmap?> = _profileBitmap

    private val _eventFlow = MutableSharedFlow<EditMemberEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // 초기 데이터를 저장하기 위한 변수들
    private var initialName: String = ""
    private var initialHeight: String = ""
    private var initialWeight: String = ""
    private var initialGender: Gender = Gender.MALE
    private var initialProfileImageUrl: String = ""

    init {
        getMemberProfile()
    }

    private fun getMemberProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getUserInfoUseCase().collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        with(result.data.member) {
                            // 초기 데이터 저장
                            initialName = nickname
                            initialHeight = height.toString()
                            initialWeight = weight.toString()
                            initialGender = gender
                            initialProfileImageUrl = profileImageUrl ?: ""
                            
                            _uiState.update {
                                it.copy(
                                    name = nickname,
                                    height = height.toString(),
                                    weight = weight.toString(),
                                    gender = gender,
                                    profileImageUrl = profileImageUrl ?: "",
                                    isLoading = false,
                                    hasChanges = false
                                )
                            }
                        }
                    }

                    else -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _eventFlow.emit(
                            EditMemberEvent.Error(
                                errorMessage = "네트워크 에러가 발생했습니다."
                            )
                        )
                    }
                }
            }
        }
    }

    // 수정사항이 있는지 확인하는 함수
    private fun checkForChanges() {
        val currentState = _uiState.value
        val hasChanges = currentState.name != initialName ||
                currentState.height != initialHeight ||
                currentState.weight != initialWeight ||
                currentState.gender != initialGender ||
                _profileBitmap.value != null

        _uiState.update { it.copy(hasChanges = hasChanges) }
    }

    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(
                name = newName,
                isNameError = false,
                isButtonEnabled = isFormValid(newName, it.height, it.weight)
            )
        }
        checkForChanges()
    }

    private fun filterInvalidChars(input: String): String {
        // 한글과 영문만 허용
        return input.filter { char ->
            char in '가'..'힣' || char in 'a'..'z' || char in 'A'..'Z'
        }
    }

    private fun applyLengthLimit(input: String): String {
        if (input.isBlank()) return input
        
        val isKoreanOnly = isKorean(input)
        val isEnglishOnly = isEnglish(input)
        val isMixed = isMixed(input)
        
        return when {
            isKoreanOnly -> input.take(5)
            isEnglishOnly -> input.take(7)
            isMixed -> input.take(7)
            else -> input
        }
    }

    fun onHeightChange(newHeight: String) {
        val filtered = newHeight.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                height = filtered,
                isHeightError = false,
                isButtonEnabled = isFormValid(it.name, filtered, it.weight)
            )
        }
        checkForChanges()
    }

    fun onWeightChange(newWeight: String) {
        val filtered = newWeight.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                weight = filtered,
                isWeightError = false,
                isButtonEnabled = isFormValid(it.name, it.height, filtered)
            )
        }
        checkForChanges()
    }

    fun selectGender(gender: Gender) {
        _uiState.update {
            it.copy(gender = gender)
        }
        checkForChanges()
    }

    fun setProfileBitmap(bitmap: Bitmap) {
        _profileBitmap.value = bitmap
        checkForChanges()
    }

    fun saveMemberInfo(memberImage: File?) {
        val currentState = _uiState.value

        val nameValidation = validateName(currentState.name)
        val heightValidation = validateHeight(currentState.height)
        val weightValidation = validateWeight(currentState.weight)

        if (nameValidation || heightValidation || weightValidation) {
            _uiState.update {
                it.copy(
                    isNameError = nameValidation,
                    isHeightError = heightValidation,
                    isWeightError = weightValidation
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            with(_uiState.value) {
                updateMemberUseCase(
                    memberDetail = Member(
                        nickname = name,
                        gender = gender,
                        height = height.toInt(),
                        weight = weight.toInt(),
                        profileImageUrl = ""
                    ),
                    memberImage = memberImage
                ).collect { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            _eventFlow.emit(EditMemberEvent.Success)
                        }

                        else -> {
                            _eventFlow.emit(
                                EditMemberEvent.Error(
                                    errorMessage = "네트워크 에러가 발생했습니다."
                                )
                            )
                        }
                    }

                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun isFormValid(name: String, height: String, weight: String): Boolean {
        return name.isNotBlank() && height.isNotBlank() && weight.isNotBlank()
    }

    private fun validateName(name: String): Boolean {
        if (name.isBlank()) return true

        if (containsInvalidChars(name)) return true

        val isKoreanOnly = isKorean(name)
        val isEnglishOnly = isEnglish(name)
        val isMixed = isMixed(name)

        return when {
            isKoreanOnly && name.length > 5 -> true
            isEnglishOnly && name.length > 7 -> true
            isMixed && name.length > 7 -> true
            !isKoreanOnly && !isEnglishOnly && !isMixed -> true
            else -> false
        }
    }

    private fun validateHeight(height: String): Boolean {
        if (height.isBlank()) return true

        val heightInt = height.toIntOrNull() ?: return true
        return heightInt !in HEIGHT_RANGE
    }

    private fun validateWeight(weight: String): Boolean {
        if (weight.isBlank()) return true

        val weightInt = weight.toIntOrNull() ?: return true
        return weightInt !in WEIGHT_RANGE
    }

    private fun isMixed(input: String): Boolean {
        val hasKorean = input.any { it in '\uAC00'..'\uD7A3' }
        val hasEnglish = input.any { it.isLetter() && (it in 'a'..'z' || it in 'A'..'Z') }
        return hasKorean && hasEnglish
    }

    private fun containsInvalidChars(input: String): Boolean {
        val regex = Regex("^[가-힣a-zA-Z]+$")
        return !regex.matches(input)
    }

    private fun isKorean(input: String) = input.matches(Regex("^[가-힣]+$"))
    private fun isEnglish(input: String) = input.matches(Regex("^[a-zA-Z]+$"))

    private val HEIGHT_RANGE = 91..242
    private val WEIGHT_RANGE = 10..227
}