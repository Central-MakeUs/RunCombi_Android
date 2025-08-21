package com.combo.runcombi.setting.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.domain.user.usecase.DeletePetUseCase
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.domain.user.usecase.UpdatePetUseCase
import com.combo.runcombi.setting.model.EditPetEvent
import com.combo.runcombi.setting.model.EditPetUiState
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
class EditPetViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updatePetUseCase: UpdatePetUseCase,
    private val deletePetUseCase: DeletePetUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditPetUiState())
    val uiState: StateFlow<EditPetUiState> = _uiState

    companion object {
        private const val TAG = "EditPetViewModel"
    }

    private val _profileBitmap = MutableStateFlow<Bitmap?>(null)
    val profileBitmap: StateFlow<Bitmap?> = _profileBitmap

    private val _eventFlow = MutableSharedFlow<EditPetEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun getMemberProfile(petId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getUserInfoUseCase().collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        val pet = result.data.petList.firstOrNull { pet ->
                            pet.id == petId
                        }

                        pet?.let { petData ->
                            _uiState.update {
                                it.copy(
                                    name = petData.name,
                                    age = petData.age.toString(),
                                    weight = petData.weight.toString(),
                                    profileImageUrl = petData.profileImageUrl ?: "",
                                    runStyle = petData.runStyle,
                                    isLoading = false,
                                    isRemovable = result.data.petList.size >= 2
                                )
                            }
                        }
                    }

                    else -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _eventFlow.emit(
                            EditPetEvent.Error(
                                errorMessage = "네트워크 에러가 발생했습니다."
                            )
                        )
                    }
                }
            }
        }
    }

    fun savePetInfo(petImage: File?, petId: Int) {
        val currentState = _uiState.value

        val nameValidation = validateName(currentState.name)
        val ageValidation = validateAge(currentState.age)
        val weightValidation = validateWeight(currentState.weight)

        if (nameValidation || ageValidation || weightValidation) {
            _uiState.update {
                it.copy(
                    isNameError = nameValidation,
                    isAgeError = ageValidation,
                    isWeightError = weightValidation
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            with(_uiState.value) {
                updatePetUseCase(
                    petDetail = Pet(
                        id = petId,
                        name = name,
                        age = age.toInt(),
                        weight = weight.toDouble(),
                        runStyle = runStyle,
                        profileImageUrl = ""
                    ),
                    petImage = petImage
                ).collect { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            _eventFlow.emit(EditPetEvent.Success)
                        }

                        else -> {
                            _eventFlow.emit(
                                EditPetEvent.Error(
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

    fun deletePet(petId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            deletePetUseCase(
                petId = petId
            ).collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _eventFlow.emit(EditPetEvent.Success)
                    }

                    else -> {
                        _eventFlow.emit(
                            EditPetEvent.Error(
                                errorMessage = "네트워크 에러가 발생했습니다."
                            )
                        )
                    }
                }

            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun setProfileBitmap(bitmap: Bitmap) {
        _profileBitmap.value = bitmap
    }

    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(
                name = newName,
                isNameError = false
            )
        }
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

    private fun isKorean(input: String) = input.matches(Regex("^[가-힣]+$"))
    private fun isEnglish(input: String) = input.matches(Regex("^[a-zA-Z]+$"))
    private fun isMixed(input: String): Boolean {
        val hasKorean = input.any { it in '\uAC00'..'\uD7A3' }
        val hasEnglish = input.any { it.isLetter() && (it in 'a'..'z' || it in 'A'..'Z') }
        return hasKorean && hasEnglish
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

    private fun containsInvalidChars(input: String): Boolean {
        val regex = Regex("^[가-힣a-zA-Z]+$")
        return !regex.matches(input)
    }

    fun onSelectRunStyle(runStyle: RunStyle) {
        _uiState.update {
            it.copy(
                runStyle = runStyle
            )
        }
    }

    fun onAgeChange(newAge: String) {
        val filtered = newAge.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                age = filtered,
                isAgeError = false
            )
        }
    }

    fun onWeightChange(newWeight: String) {
        var filtered = newWeight.filter { it.isDigit() || it == '.' }
        val dotCount = filtered.count { it == '.' }
        if (dotCount > 1) {
            filtered = filtered.substring(0, filtered.lastIndexOf('.'))
        }
        if (filtered.startsWith(".")) filtered = ""
        if (filtered.contains('.')) {
            val parts = filtered.split('.')
            filtered = parts[0] + "." + parts.getOrNull(1)?.take(1).orEmpty()
        }
        _uiState.update {
            it.copy(
                weight = filtered,
                isWeightError = false
            )
        }
    }

    private fun validateAge(age: String): Boolean {
        if (age.isBlank()) return true
        val ageInt = age.toIntOrNull()
        return ageInt == null || ageInt !in 1..25
    }

    private fun validateWeight(weight: String): Boolean {
        if (weight.isBlank()) return true
        val weightFloat = weight.toFloatOrNull()
        if (weightFloat == null || weightFloat < 0.5f || weightFloat > 100f) {
            return true
        }
        if (weight.contains('.')) {
            val parts = weight.split('.')
            if ((parts.getOrNull(1)?.length ?: 0) > 1) {
                return true
            }
        }
        return false
    }

    private fun isValid(age: String, weight: String): Boolean {
        return age.isNotBlank() && weight.isNotBlank()
    }

    private fun validate(age: String, weight: String): Pair<Boolean, String> {
        if (age.isBlank() || weight.isBlank()) {
            return true to "나이와 몸무게를 모두 입력해주세요."
        }
        val ageInt = age.toIntOrNull()
        val weightFloat = weight.toFloatOrNull()
        if (ageInt == null || ageInt !in 1..25) {
            return true to "나이는 1~25살 사이로 입력해주세요."
        }
        if (weightFloat == null || weightFloat < 0.5f || weightFloat > 100f) {
            return true to "몸무게는 0.5~100kg 사이로 입력해주세요."
        }
        if (weight.contains('.')) {
            val parts = weight.split('.')
            if ((parts.getOrNull(1)?.length ?: 0) > 1) {
                return true to "몸무게는 소수점 1자리까지만 입력 가능합니다."
            }
        }
        return false to ""
    }
}