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
                isButtonEnabled = newName.isNotBlank() && newName.length <= 10
            )
        }
    }

    fun setProfileBitmap(bitmap: Bitmap) {
        _profileBitmap.value = bitmap
    }

    fun validateAndProceed(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        if (currentState.name.isBlank()) {
            _uiState.update {
                it.copy(isError = true)
            }
            return
        }
        
        if (currentState.name.length > 10) {
            _uiState.update {
                it.copy(isError = true)
            }
            return
        }

        onSuccess()
    }
} 