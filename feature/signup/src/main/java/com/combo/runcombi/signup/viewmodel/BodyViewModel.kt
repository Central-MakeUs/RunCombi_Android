package com.combo.runcombi.signup.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.signup.model.BodyUiState
import com.combo.runcombi.domain.user.model.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BodyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BodyUiState())
    val uiState: StateFlow<BodyUiState> = _uiState

    fun setDefaultValues(gender: Gender) {
        val defaultHeight = when (gender) {
            Gender.MALE -> "172"
            Gender.FEMALE -> "160"
        }
        val defaultWeight = when (gender) {
            Gender.MALE -> "68"
            Gender.FEMALE -> "55"
        }
        _uiState.value = _uiState.value.copy(
            height = defaultHeight,
            weight = defaultWeight,
            isButtonEnabled = true,
            isError = false,
            errorMessage = ""
        )
    }

    fun onHeightChange(newHeight: String, gender: Gender) {
        // 숫자만 허용, 소수점 불가
        val filtered = newHeight.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                height = filtered,
                isButtonEnabled = isValid(filtered, it.weight),
                isError = false,
                errorMessage = ""
            )
        }
    }

    fun onWeightChange(newWeight: String, gender: Gender) {
        // 숫자만 허용, 소수점 불가
        val filtered = newWeight.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                weight = filtered,
                isButtonEnabled = isValid(it.height, filtered),
                isError = false,
                errorMessage = ""
            )
        }
    }

    fun validateAndProceed(gender: Gender, onSuccess: () -> Unit) {
        val height = uiState.value.height
        val weight = uiState.value.weight
        val (hasError, message) = validate(height, weight, gender)
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

    private fun validate(height: String, weight: String, gender: Gender): Pair<Boolean, String> {
        if (height.isBlank() || weight.isBlank()) {
            return true to "키와 몸무게를 모두 입력해주세요."
        }
        val heightInt = height.toIntOrNull()
        val weightInt = weight.toIntOrNull()
        // 성별별 범위 적용
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