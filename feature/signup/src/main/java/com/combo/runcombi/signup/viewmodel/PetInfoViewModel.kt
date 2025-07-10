package com.combo.runcombi.signup.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.signup.model.PetInfoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PetInfoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PetInfoUiState())
    val uiState: StateFlow<PetInfoUiState> = _uiState

    fun onAgeChange(newAge: String) {
        _uiState.update {
            it.copy(
                age = newAge,
                isButtonEnabled = isValid(newAge, it.weight),
                isError = false,
                errorMessage = ""
            )
        }
    }

    fun onWeightChange(newWeight: String) {
        _uiState.update {
            it.copy(
                weight = newWeight,
                isButtonEnabled = isValid(it.weight, newWeight),
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

        val weightInt = weight.toIntOrNull()

        if (weightInt == null || weightInt !in 10..100) {
            return true to "몸무게는 10~100kg 사이로 입력해주세요."
        }
        return false to ""
    }

    fun clear() {
        _uiState.value = PetInfoUiState()
    }
}