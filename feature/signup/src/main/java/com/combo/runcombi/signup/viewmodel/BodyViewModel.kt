package com.combo.runcombi.signup.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.signup.model.BodyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BodyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BodyUiState())
    val uiState: StateFlow<BodyUiState> = _uiState

    fun setDefaultValues(gender: Gender) {
        _uiState.value = _uiState.value.copy(
            height = "",
            weight = "",
            isButtonEnabled = false,
            isError = false,
            errorMessage = "",
            isHeightError = false,
            isWeightError = false
        )
    }

    fun onHeightChange(newHeight: String, gender: Gender) {
        val filtered = newHeight.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                height = filtered,
                isButtonEnabled = isValid(filtered, it.weight),
                isError = false,
                errorMessage = "",
                isHeightError = false
            )
        }
    }

    fun onWeightChange(newWeight: String, gender: Gender) {
        val filtered = newWeight.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                weight = filtered,
                isButtonEnabled = isValid(it.height, filtered),
                isError = false,
                errorMessage = "",
                isWeightError = false
            )
        }
    }

    fun validateAndProceed(gender: Gender, onSuccess: () -> Unit) {
        val height = uiState.value.height
        val weight = uiState.value.weight
        
        val heightValidation = validateHeight(height)
        val weightValidation = validateWeight(weight)
        
        if (heightValidation || weightValidation) {
            _uiState.update {
                it.copy(
                    isError = true,
                    isHeightError = heightValidation,
                    isWeightError = weightValidation
                )
            }
            return
        }
        
        _uiState.update {
            it.copy(
                isError = false,
                isHeightError = false,
                isWeightError = false
            )
        }
        onSuccess()
    }

    private fun isValid(height: String, weight: String): Boolean {
        return height.isNotBlank() && weight.isNotBlank()
    }

    private fun validateHeight(height: String): Boolean {
        if (height.isBlank()) return true
        val heightInt = height.toIntOrNull()
        return heightInt == null || heightInt !in 91..242
    }

    private fun validateWeight(weight: String): Boolean {
        if (weight.isBlank()) return true
        val weightInt = weight.toIntOrNull()
        return weightInt == null || weightInt !in 10..227
    }

    private fun validate(height: String, weight: String, gender: Gender): Pair<Boolean, String> {
        if (height.isBlank() || weight.isBlank()) {
            return true to "키와 몸무게를 모두 입력해주세요."
        }
        val heightInt = height.toIntOrNull()
        val weightInt = weight.toIntOrNull()

        val heightRange = 91..242
        val weightRange = 10..227

        if (heightInt == null || heightInt !in heightRange) {
            return true to "키는 91~242cm 사이로 입력해주세요."
        }
        if (weightInt == null || weightInt !in weightRange) {
            return true to "몸무게는 10~227kg 사이로 입력해주세요."
        }
        return false to ""
    }
}