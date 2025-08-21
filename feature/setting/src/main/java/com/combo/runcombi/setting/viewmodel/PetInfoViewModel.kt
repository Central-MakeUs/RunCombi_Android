package com.combo.runcombi.setting.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.setting.model.PetInfoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PetInfoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        PetInfoUiState(
            age = "",
            weight = "",
            isButtonEnabled = false,
            isError = false,
            errorMessage = "",
            isAgeError = false,
            isWeightError = false
        )
    )
    val uiState: StateFlow<PetInfoUiState> = _uiState

    fun onAgeChange(newAge: String) {
        val filtered = newAge.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                age = filtered,
                isButtonEnabled = isValid(filtered, it.weight),
                isError = false,
                errorMessage = "",
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
                isButtonEnabled = isValid(filtered, it.age),
                isError = false,
                errorMessage = "",
                isWeightError = false
            )
        }
    }

    fun validateAndProceed(onSuccess: () -> Unit) {
        val age = uiState.value.age
        val weight = uiState.value.weight
        
        val ageValidation = validateAge(age)
        val weightValidation = validateWeight(weight)
        
        if (ageValidation || weightValidation) {
            _uiState.update {
                it.copy(
                    isError = true,
                    isAgeError = ageValidation,
                    isWeightError = weightValidation
                )
            }
            return
        }
        
        _uiState.update {
            it.copy(
                isError = false,
                isAgeError = false,
                isWeightError = false
            )
        }
        onSuccess()
    }

    private fun isValid(age: String, weight: String): Boolean {
        return age.isNotBlank() && weight.isNotBlank()
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