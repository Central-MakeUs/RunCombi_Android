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

    private fun validateName(input: String): Pair<Boolean, String> {
        return when {
            input.isBlank() -> true to "이름을 입력해주세요."
            isKorean(input) && input.length > 6 -> true to "한글 6자 이하로 입력해주세요."
            isEnglish(input) && input.length > 10 -> true to "영문 10자 이하로 입력해주세요."
            !isKorean(input) && !isEnglish(input) -> true to "한글 또는 영문만 입력 가능합니다."
            else -> false to ""
        }
    }

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

    private fun isKorean(input: String) = input.matches(Regex("^[가-힣]+$"))
    private fun isEnglish(input: String) = input.matches(Regex("^[a-zA-Z]+$"))

    fun onImageSelected(bitmap: Bitmap) {
        _profileBitmap.value = bitmap
        emitEvent(ProfileEvent.ImageSelected(bitmap))
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