package com.combo.runcombi.signup.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.signup.model.PetInfoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PetInfoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        PetInfoUiState(
            age = "5",
            weight = "5.5",
            isButtonEnabled = true,
            isError = false,
            errorMessage = ""
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
                errorMessage = ""
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
                isButtonEnabled = isValid(it.age, filtered),
                isError = false,
                errorMessage = ""
            )
        }
    }

    fun validateAndProceed(onSuccess: () -> Unit) {
        val age = uiState.value.age
        val weight = uiState.value.weight
        val (hasError, message) = validate(age, weight)
        if (hasError) {
            _uiState.update {
                it.copy(isError = true, errorMessage = message)
            }
        } else {
            _uiState.update {
                it.copy(isError = false, errorMessage = "")
            }
            onSuccess()
        }
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