package com.combo.runcombi.signup.screen

import androidx.lifecycle.ViewModel
import com.combo.runcombi.signup.model.BodyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BodyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BodyUiState())
    val uiState: StateFlow<BodyUiState> = _uiState

    fun onHeightChange(newHeight: String) {
        _uiState.update {
            it.copy(
                height = newHeight,
                isButtonEnabled = isValid(newHeight, it.weight),
                isError = false,
                errorMessage = ""
            )
        }
    }

    fun onWeightChange(newWeight: String) {
        _uiState.update {
            it.copy(
                weight = newWeight,
                isButtonEnabled = isValid(it.height, newWeight),
                isError = false,
                errorMessage = ""
            )
        }
    }

    fun validateAndProceed(onSuccess: () -> Unit) {
        val height = uiState.value.height
        val weight = uiState.value.weight
        val (hasError, message) = validate(height, weight)
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

    private fun isValid(height: String, weight: String): Boolean {
        return height.isNotBlank() && weight.isNotBlank()
    }

    private fun validate(height: String, weight: String): Pair<Boolean, String> {
        if (height.isBlank() || weight.isBlank()) {
            return true to "키와 몸무게를 모두 입력해주세요."
        }
        val heightInt = height.toIntOrNull()
        val weightInt = weight.toIntOrNull()
        if (heightInt == null || heightInt !in 50..250) {
            return true to "키는 50~250cm 사이로 입력해주세요."
        }
        if (weightInt == null || weightInt !in 10..300) {
            return true to "몸무게는 10~300kg 사이로 입력해주세요."
        }
        return false to ""
    }

    fun clear() {
        _uiState.value = BodyUiState()
    }
} 