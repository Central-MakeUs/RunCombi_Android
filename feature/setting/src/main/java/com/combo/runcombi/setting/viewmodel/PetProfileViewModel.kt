package com.combo.runcombi.setting.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.combo.runcombi.setting.model.PetProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PetProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PetProfileUiState())
    val uiState: StateFlow<PetProfileUiState> = _uiState

    private val _profileBitmap = MutableStateFlow<Bitmap?>(null)
    val profileBitmap: StateFlow<Bitmap?> = _profileBitmap

    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(
                name = newName,
                isError = false,
                isButtonEnabled = newName.isNotBlank()
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

    fun validateAndProceed(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        if (currentState.name.isBlank()) {
            _uiState.update {
                it.copy(isError = true)
            }
            return
        }
        
        if (containsInvalidChars(currentState.name)) {
            _uiState.update {
                it.copy(isError = true)
            }
            return
        }

        val isKoreanOnly = isKorean(currentState.name)
        val isEnglishOnly = isEnglish(currentState.name)
        val isMixed = isMixed(currentState.name)

        val isValidLength = when {
            isKoreanOnly -> currentState.name.length <= 5
            isEnglishOnly -> currentState.name.length <= 7
            isMixed -> currentState.name.length <= 7
            else -> false
        }

        if (!isValidLength) {
            _uiState.update {
                it.copy(isError = true)
            }
            return
        }

        onSuccess()
    }

    private fun containsInvalidChars(input: String): Boolean {
        val regex = Regex("^[가-힣a-zA-Z]+$")
        return !regex.matches(input)
    }

    fun setProfileBitmap(bitmap: Bitmap) {
        _profileBitmap.value = bitmap
    }
} 