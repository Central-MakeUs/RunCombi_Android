package com.combo.runcombi.signup.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.signup.model.PermissionType
import com.combo.runcombi.signup.model.ProfileEvent
import com.combo.runcombi.signup.model.ProfileUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<ProfileEvent>()
    val eventFlow: SharedFlow<ProfileEvent> = _eventFlow.asSharedFlow()

    private val _profileBitmap = MutableStateFlow<Bitmap?>(null)
    val profileBitmap: StateFlow<Bitmap?> = _profileBitmap

    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(
                name = newName,
                isButtonEnabled = newName.isNotBlank(),
                isError = false,
                errorMessage = ""
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

    private fun validateName(input: String): Pair<Boolean, String> {
        if (input.isBlank()) {
            return true to "이름을 입력해주세요."
        }
        if (containsInvalidChars(input)) {
            return true to "한글과 영문만 입력할 수 있어요!"
        }

        val isKoreanOnly = isKorean(input)
        val isEnglishOnly = isEnglish(input)
        val isMixed = isMixed(input)

        return when {
            isKoreanOnly && input.length > 5 -> true to "한글은 최대 5자까지 입력할 수 있어요!"
            isEnglishOnly && input.length > 7 -> true to "이름은 최대 7자까지 입력할 수 있어요!"
            isMixed && input.length > 7 -> true to "이름은 최대 7자까지 입력할 수 있어요!"
            !isKoreanOnly && !isEnglishOnly && !isMixed -> true to "한글 또는 영문만 입력 가능합니다."
            else -> false to ""
        }
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


    fun validateAndProceed(onSuccess: () -> Unit) {
        val input = uiState.value.name
        val (hasError, message) = validateName(input)
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

    fun setProfileBitmap(bitmap: Bitmap) {
        _profileBitmap.value = bitmap
    }

    fun onCameraButtonClick() = emitEvent(ProfileEvent.ShowImagePickerBottomSheet)
    fun onSelectCamera() = emitEvent(ProfileEvent.RequestCameraPermission)
    fun openAlbum() = emitEvent(ProfileEvent.OpenAlbum)
    fun openCamera() = emitEvent(ProfileEvent.OpenCamera)
    fun onPermissionDenied(type: PermissionType) = emitEvent(ProfileEvent.PermissionDenied(type))
    fun onError() = emitEvent(ProfileEvent.Error)

    private fun emitEvent(event: ProfileEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }
}